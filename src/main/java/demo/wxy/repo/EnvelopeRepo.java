package demo.wxy.repo;

import demo.wxy.entity.Envelope;
import demo.wxy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * @author gnayuxiew
 */
public interface EnvelopeRepo extends JpaRepository<Envelope, Integer> {
    /**
     * 根据用户找到该用户发的所有红包
     * @param user 用户
     * @return 红包列表
     *
     */
    Set<Envelope> findEnvelopesByUser(User user);
}
