package demo.wxy.controller;

import demo.wxy.entity.Envelope;
import demo.wxy.entity.Packet;
import demo.wxy.service.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gnayuxiew
 * 查看小红包的具体信息，即显示这个小红包是属于哪个大红包的，这个大红包被抢信息
 */
@Controller
@RequestMapping("/packet")
public class PacketController {

    @Autowired
    private PacketService packetService;

    @GetMapping("/{packet_id}")
    public String showPacketInfo(Model model, @PathVariable("packet_id") String packetId) {
        Packet p = packetService.find(Integer.valueOf(packetId));
        if (p != null) {
            Envelope envelope = p.getPacketEnvelope();
            List<Packet> packets = envelope.getEnvelopePackets();
            Map<Integer, Double> map = new HashMap<>();
            for (Packet packet : packets) {
                if (packet.getPacketUser() != null) {
                    map.put(packet.getPacketUser().getUserId(), packet.getPacketValue());
                }
            }
            model.addAttribute("record", map);
            model.addAttribute("success", "true");
        } else {
            model.addAttribute("success", "false");
        }
        return "envelope/get";
    }
}
