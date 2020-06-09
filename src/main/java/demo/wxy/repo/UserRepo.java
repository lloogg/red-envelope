package demo.wxy.repo;

import demo.wxy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gnayuxiew
 */
public interface UserRepo extends JpaRepository<User, Integer> {
}