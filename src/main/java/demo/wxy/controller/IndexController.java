package demo.wxy.controller;

import demo.wxy.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class IndexController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/")
    public String requestRedPacket() {

        redisService.rush(UUID.randomUUID().toString());
//        redisService.rush("" + new Random().nextInt(10));
        return "success";
    }

    @GetMapping("/a")
    public void a() {
        redisService.a();
    }
}
