package demo.wxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gnayuxiew
 */
@Service
public class RedisService {
    AtomicInteger a = new AtomicInteger();
    int b = 0;
    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void rush(String tel) {
        checkAndRush(tel);
    }


    public synchronized void checkAndRush(String tel) {
        a.getAndIncrement();
        b++;
        if (!redisTemplate.opsForSet().isMember("success", tel)) {
            String packetId = (String) redisTemplate.opsForList().leftPop("red");
            if (StringUtils.isEmpty(packetId)) {
//                logger.info(tel + "没有抢到红包");
                redisTemplate.opsForSet().add("fail", tel);
            } else {
//                logger.info(tel + "抢到红包！");
                redisTemplate.opsForSet().add("success", tel);
            }
        } else {
            redisTemplate.opsForSet().add("fail", tel);
        }
    }

    public void a() {
        System.out.println(a.get());
        System.out.println(b);
    }
}
