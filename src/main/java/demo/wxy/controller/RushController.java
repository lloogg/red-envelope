package demo.wxy.controller;

import demo.wxy.service.RushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gnayuxiew
 */
@RestController
public class RushController {

    @Autowired
    private RushService rushService;

    @GetMapping("/rush/{envelope_id}")
    public void rush(@PathVariable("envelope_id") Integer envelopeId) {
        rushService.rush(envelopeId);
    }
}
