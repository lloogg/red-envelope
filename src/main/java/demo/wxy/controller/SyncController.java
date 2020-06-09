package demo.wxy.controller;

import demo.wxy.service.UserService;
import demo.wxy.entity.Packet;
import demo.wxy.entity.User;
import demo.wxy.service.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SyncController {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PacketService packetService;

    @Autowired
    private UserService userService;

    @GetMapping("/sync/{envelope_id}")
    public void sync(@PathVariable("envelope_id") String envelopeId) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(envelopeId + "_detail");
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            Integer userId = Integer.valueOf((String) entry.getKey());
            Integer packetId = Integer.valueOf((String) entry.getValue());


            Packet packet = packetService.find(packetId);
            User user = userService.find(userId);
            user.setBalance(user.getBalance() + packet.getPacketValue());

            packet.setPacketUser(user);
            packetService.save(packet);
        }
    }
}
