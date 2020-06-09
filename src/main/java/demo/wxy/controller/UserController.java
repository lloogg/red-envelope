package demo.wxy.controller;

import demo.wxy.entity.User;
import demo.wxy.service.UserService;
import demo.wxy.service.EnvelopeService;
import demo.wxy.util.PacketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @author gnayuxiew
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PacketUtil packetUtil;
    @Autowired
    private EnvelopeService envelopeService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/{user_id}")
    public String getInfo(Model model, @PathVariable("user_id") Integer userId) {
        User user = userService.find(userId);
        model.addAttribute("user_id", user.getUserId());
        model.addAttribute("user_send", user.getUserSend());
        model.addAttribute("user_received", user.getUserReceived());
        model.addAttribute("user_balance", user.getBalance());
        return "user";
    }

    @GetMapping("/{user_id}/send")
    public String sendEnvelope(Model model, @PathVariable("user_id") Integer userId, @PathParam("sum") Integer sum, @PathParam("count") Integer count) {

        User user = userService.sendEnvelope(userId, sum, count);


//        key为大红包id，使用envelope_前缀，值为小红包id列表
        return "redirect:/user/" + user.getUserId();
    }



//    @GetMapping("/{user_id}/sent")
//    public String getSent(Model model, @PathVariable("user_id") String userId) {
//        User user = userService.find(userId);
//    }
//
//    @GetMapping("/{user_id}/received")
//    public String getReceived(Model model, @PathVariable("user_id") String userId) {
//
//    }

    @GetMapping("/send")
    public String send(Model model, @RequestParam("user_id") Integer userId) {
        model.addAttribute("userId", userId);
        return "send";
    }
}
