package demo.wxy.service;

import demo.wxy.entity.Packet;
import demo.wxy.repo.PacketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gnayuxiew
 */
@Service
public class PacketService {
    @Autowired
    private PacketRepo packetRepo;

    public Packet save(Packet packet) {
        return packetRepo.save(packet);
    }

    public Packet find(Integer packetId) {
        return packetRepo.findById(packetId).orElse(null);
    }
}
