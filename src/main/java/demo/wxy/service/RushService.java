package demo.wxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;

/**
 * @author gnayuxiew
 */
@Service
public class RushService {
    @Autowired
    private EnvelopeService envelopeService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private PacketService packetService;


    @Autowired
    private UserService userService;



    public synchronized void rush(Integer envelopeId) {
        Random random = new Random();
//        用户id为随机数
        Integer userId = random.nextInt(1000) + 1;
        Set<String> set = redisTemplate.keys("envelope_" + envelopeId);
        if (set != null && set.size() > 0) {

            if (!redisTemplate.opsForSet().isMember(envelopeId + "_success", userId)) {
                redisTemplate.opsForSet().add(envelopeId + "_success", userId);
                String packetId = (String) redisTemplate.opsForList().leftPop("envelope_" + envelopeId);
                Integer id = Integer.valueOf(packetId);
                redisTemplate.opsForHash().put(envelopeId + "_detail", String.valueOf(userId), packetId);
//                saveToDb(id, userId);
            }
        }
    }

//    @Async
//    public void saveToDb(Integer id, Integer userId) {
//        Packet packet = packetService.find(id);
//        User user = userService.find(userId);
//        user.setBalance(user.getBalance() + packet.getPacketValue());
//
//        packet.setPacketUser(user);
//        packet = packetService.save(packet);
//
//        user.getUserReceived().add(packet);
//        userService.save(user);
//    }
}
