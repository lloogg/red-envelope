package demo.wxy.service;

import demo.wxy.entity.Envelope;
import demo.wxy.entity.Packet;
import demo.wxy.entity.User;
import demo.wxy.repo.UserRepo;
import demo.wxy.util.PacketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author gnayuxiew
 */
@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PacketUtil packetUtil;

    @Autowired
    private EnvelopeService envelopeService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public User find(Integer userId) {
        Optional<User> user = userRepo.findById(userId);
        return user.orElse(null);
    }

    /**
     * @param userId 谁发的红包
     * @param sum    红包金额
     * @param count  红包数量
     *               用户发红包，初始化小红包，把红包信息存入数据库和redis
     */
    public User sendEnvelope(Integer userId, Integer sum, Integer count) {
        User user = find(userId);
        if (null == user) {
            throw new NullPointerException("用户不存在");
        }
        if (user.getBalance() < sum) {
            throw new IllegalArgumentException("余额不足");
        }

//        包红包
        Envelope envelope = new Envelope();
        envelope.setEnvelopeSum(sum);
        envelope.setEnvelopeCount(count);

        List<Packet> packets = packetUtil.getPackets(envelope, sum, count);

        envelope.setEnvelopePackets(packets);
        envelope.setUser(user);

        envelopeService.save(envelope);
//        扣掉余额
        user.setBalance(user.getBalance() - sum);
        user.getUserSend().add(envelope);
        user = save(user);

        List<String> list = new ArrayList<>();
        for (Packet packet : packets) {
            list.add(packet.getPacketId().toString());
        }

        redisTemplate.opsForList().leftPushAll("envelope_" + envelope.getEnvelopeId(), list.toArray());
        //用户扣去金额

        return user;
    }

    public User save(User user) {
        return userRepo.save(user);
    }
}
