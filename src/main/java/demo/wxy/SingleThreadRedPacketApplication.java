package demo.wxy;

import demo.wxy.entity.User;
import demo.wxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author gnayuxiew
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling

public class SingleThreadRedPacketApplication {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(SingleThreadRedPacketApplication.class, args);
    }

    @PostConstruct
    public void init() {
//        初始化1000个用户
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setBalance(100.0);
            userService.save(user);
        }

//清空缓存
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }
}
