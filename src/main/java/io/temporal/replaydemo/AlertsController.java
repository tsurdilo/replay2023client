package io.temporal.replaydemo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlertsController {
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload) {
        // Handle the incoming webhook request here
        System.out.println("******************** PAYLOAD: " + payload);
        return ResponseEntity.ok().build();
    }
}
