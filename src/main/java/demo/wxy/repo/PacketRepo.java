package demo.wxy.repo;

import demo.wxy.entity.Packet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gnayuxiew
 */
public interface PacketRepo extends JpaRepository<Packet, Integer> {
}
