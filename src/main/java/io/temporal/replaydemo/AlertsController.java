package io.temporal.replaydemo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class AlertsController {
    @Autowired EmitterController emitterController;

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload) {
        System.out.println("*********** PAYLOAD: " + payload);
        String response = "";
        try {
            JsonNode arrNode = new ObjectMapper().readTree(payload).get("alerts");
            if (arrNode.isArray()) {
                JsonNode first = arrNode.get(0);
                response += first.get("labels").get("rulename").asText();
                response += "#" + first.get("startsAt").asText();
                response += "#" + first.get("dashboardURL").asText();
            }
        } catch (Exception e) {
            response = "Error parsing payload: " + e.getMessage();
        }

        SseEmitter latestEm = emitterController.getLatestEmitter();

        try {
            System.out.println("************ RES: " + response);
            latestEm.send(response);
        } catch (IOException e) {
            latestEm.completeWithError(e);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/alerts")
    public SseEmitter getAlerts() {
        SseEmitter emitter = new SseEmitter(-1L);
        emitterController.addEmitter(emitter);

        emitter.onCompletion(() -> emitterController.getEmitters().remove(emitter));

        emitter.onTimeout(() -> emitterController.getEmitters().remove(emitter));

        return emitter;
    }
}
