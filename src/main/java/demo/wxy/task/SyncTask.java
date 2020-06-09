package demo.wxy.task;

import demo.wxy.entity.User;
import demo.wxy.service.UserService;
import demo.wxy.entity.Packet;
import demo.wxy.service.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author gnayuxiew
 */
@Component
public class SyncTask {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PacketService packetService;

    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 2000)
    public void sync() {
        Set<String> set = redisTemplate.keys("*_detail");
        if (set != null) {
            for (String s : set) {
//                System.out.println(string);

                Map<Object, Object> map = redisTemplate.opsForHash().entries(s);
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    Integer userId = Integer.valueOf((String) entry.getKey());
                    Integer packetId = Integer.valueOf((String) entry.getValue());


                    Packet packet = packetService.find(packetId);

                    if (packet.getPacketUser() == null) {
                        User user = userService.find(userId);
                        user.setBalance(user.getBalance() + packet.getPacketValue());
                        packet.setPacketUser(user);
                        packetService.save(packet);
                        userService.save(user);

                    }
                }
            }
        }


    }
}
