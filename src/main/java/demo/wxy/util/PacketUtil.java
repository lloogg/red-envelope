package demo.wxy.util;

import demo.wxy.entity.Envelope;
import demo.wxy.entity.Packet;
import demo.wxy.service.EnvelopeService;
import demo.wxy.service.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author gnayuxiew
 * 用于生成小红包
 */
@Component
public class PacketUtil {

    @Autowired
    private PacketService packetService;

    @Autowired
    private EnvelopeService envelopeService;

    public List<Packet> getPackets(Envelope envelope, Integer sum, Integer count) {
        List<Packet> packets = new ArrayList<>();
        sum *= 100;
        if (count > sum) {
            throw new IllegalArgumentException("红包无法分给这么多人");
        }
        Random random = new Random();
        int x = count;
        for (int i = 0; i < count - 1; i++) {
            int s = random.nextInt(sum / x * 2 - 1) + 1;
            sum -= s;
            x--;

            Packet packet = new Packet();
            packet.setPacketValue(s / 100.0);
            packet.setPacketEnvelope(envelope);
            packets.add(packet);
        }
        Packet packet = new Packet();
        packet.setPacketValue(sum / 100.0);
        packet.setPacketEnvelope(envelope);
        packets.add(packet);
        return packets;

    }
}
