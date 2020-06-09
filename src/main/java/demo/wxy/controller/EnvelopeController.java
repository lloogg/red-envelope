package demo.wxy.controller;

import demo.wxy.entity.Envelope;
import demo.wxy.entity.Packet;
import demo.wxy.entity.User;
import demo.wxy.service.PacketService;
import demo.wxy.service.UserService;
import demo.wxy.service.EnvelopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @author gnayuxiew
 */
@Controller
@RequestMapping("/envelope")
public class EnvelopeController {
    @Autowired
    private EnvelopeService envelopeService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private PacketService packetService;

    @Autowired
    private UserService userService;

    /**
     * @param model 模型
     *              显示redis中的所有红包
     * @return 页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<Envelope> envelopeList = envelopeService.getList();
        model.addAttribute("all_envelope", envelopeList);
        return "envelope/list";
    }

    @GetMapping("/detail/{envelope_id}")
    public String detail(Model model, @PathVariable("envelope_id") Integer envelopeId) {

//        Map<Object, Object> map =redisTemplate.opsForHash().entries(envelopeId + "_detail");



        Envelope envelope = envelopeService.find(envelopeId);
        List<Packet> packets = envelope.getEnvelopePackets();
        Map<Integer, Double> map = new HashMap<>();
        for (Packet packet : packets) {
            if (packet.getPacketUser() != null) {
                map.put(packet.getPacketUser().getUserId(), packet.getPacketValue());
            }
        }
        model.addAttribute("record", map);

        model.addAttribute("success", "true");
        return "envelope/get";

    }

    /**
     * @param model      模型
     * @param envelopeId 红包id
     * @param userId     用户id
     *                   首先判断该用户是否抢过该红包，在redis的envelope_id+”success"中查找是否存在，如果没有，就尝试进入同步代码，使用synchronized修饰的方法。
     *                   判断envelop_id列表是否为空，如果为空，提示用户手速慢，没抢到。如果不为空，将用户放入envelope_id+”success"键中，并且将该用户和子红包写入数据库。
     *                   显示红包详细信息时，可以从数据库中查找该envelope_id，再查找子红包列表中每个子红包对应的用户，由于将抢红包成功的用户写入数据库和
     *                   显示红包纤细信息不影响抢红包的核心逻辑，因此可以通过异步的方式读写数据库。
     * @return 页面名称
     */
    @GetMapping("/get/{envelope_id}/{user_id}")
    public synchronized String get(Model model, @PathVariable("envelope_id") Integer envelopeId, @PathVariable("user_id") Integer userId) {
//        如果已经抢成功过一次
        if (redisTemplate.opsForSet().isMember(envelopeId + "_success", userId)) {
            model.addAttribute("success", "already");
        } else {
//            如果红包还在
            Set<String> set = redisTemplate.keys("envelope_" + envelopeId);
            if (set != null && set.size() > 0) {
//                放入成功列表
                redisTemplate.opsForSet().add(envelopeId + "_success", userId);
//                弹出一个小红包
                String packetId = (String) redisTemplate.opsForList().leftPop("envelope_" + envelopeId);
                Integer id = Integer.valueOf(packetId);

//                saveToDb(packetId, Integer.valueOf(userId));
//                写入数据库
                Packet packet = packetService.find(id);
                User user = userService.find(userId);
                user.setBalance(user.getBalance() + packet.getPacketValue());

                packet.setPacketUser(user);
                packet = packetService.save(packet);

                user.getUserReceived().add(packet);
                userService.save(user);
                showDetail(model, envelopeId);
                model.addAttribute("success", "true");
            } else {
//                如果键不存在了，那么抢红包就失败了
                model.addAttribute("success", "false");
            }
        }
        return "envelope/get";
    }


    /**
     * 随机userId，用于压测
     *
     * @param model      模型
     * @param envelopeId 大红包id
     * @return
     */
    @GetMapping("/get/{envelope_id}/random")
    public synchronized String rush(Model model, @PathVariable("envelope_id") Integer envelopeId) {
        Random random = new Random();
        Integer userId = random.nextInt(100) + 1;
        if (redisTemplate.opsForSet().isMember(envelopeId + "_success", userId)) {
            model.addAttribute("success", "already");
        } else {
            Set<String> set = redisTemplate.keys("envelope_" + envelopeId);
            if (set != null && set.size() > 0) {
//                放入成功列表
                redisTemplate.opsForSet().add(envelopeId + "_success", userId);
//                弹出一个小红包
                String packetId = (String) redisTemplate.opsForList().leftPop("envelope_" + envelopeId);
                Integer id = Integer.valueOf(packetId);

//                saveToDb(packetId, Integer.valueOf(userId));
//                写入数据库
                saveToDb(id, userId);
//                showDetail(model, envelopeId);
                model.addAttribute("success", "true");
            } else {
//                如果键不存在了，那么抢红包就失败了
                model.addAttribute("success", "false");
            }
        }
        return "envelope/get";

    }

    /**
     * @param id     小红包id
     * @param userId 用户id
     *               将该用户抢到该小红包的记录异步写入数据库，防止在线程同步中花费
     */
    @Async
    public void saveToDb(Integer id, Integer userId) {
        Packet packet = packetService.find(id);
        User user = userService.find(userId);
        user.setBalance(user.getBalance() + packet.getPacketValue());

        packet.setPacketUser(user);
        packet = packetService.save(packet);

        user.getUserReceived().add(packet);
        userService.save(user);
    }

    @Async
    public void showDetail(Model model, Integer envelopeId) {
        Envelope envelope = envelopeService.find(envelopeId);
//        列出所有的子红包
        List<Packet> packets = envelope.getEnvelopePackets();
//        如果子红包所属的用户不为null，那就说明还没被抢过，就显示
//        第一个Integer指的是用户id，第二个Double指的是子红包的金额
        Map<Integer, Double> map = new HashMap<>();
        for (Packet packet : packets) {
            if (packet.getPacketUser() != null) {
                map.put(packet.getPacketUser().getUserId(), packet.getPacketValue());
            }
        }
        model.addAttribute("record", map);
    }


}
