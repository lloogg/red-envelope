package demo.wxy.service;

import demo.wxy.entity.Envelope;
import demo.wxy.entity.User;
import demo.wxy.repo.EnvelopeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author gnayuxiew
 */
@Service
public class EnvelopeService {
    @Autowired
    private EnvelopeRepo envelopeRepo;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void save(Envelope envelope) {
        envelopeRepo.save(envelope);
    }

    /**
     * @return 在redis中找到所有大红包
     */
    public List<Envelope> getList() {
        Set<String> list = redisTemplate.keys("envelope_*");
        List<Envelope> envelopeList = new ArrayList<>();

        if (list != null) {
            for (String s : list) {
                Integer i = Integer.valueOf(s.substring(9));
                System.out.println(i);
                envelopeRepo.findById(i).ifPresent(envelopeList::add);
            }
        }
        return envelopeList;
    }


    public Envelope find(Integer envelopeId) {
        return envelopeRepo.findById(envelopeId).orElse(null);
    }

    public Set<Envelope> findEnvelopesByUser(User user) {
        return envelopeRepo.findEnvelopesByUser(user);
    }


}
