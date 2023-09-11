package io.temporal.replaydemo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    WorkflowClient client;

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload) {
        String response = "";
        try {
            JsonNode arrNode = new ObjectMapper().readTree(payload).get("alerts");
            if (arrNode.isArray()) {
                JsonNode first = arrNode.get(0);
                if(first.get("labels").has("rulename")) {
                    response += first.get("labels").get("rulename").asText();
                }
                if(first.get("labels").has("alertname")) {
                    response += first.get("labels").get("alertname").asText();
                }
                response += "#" + first.get("startsAt").asText();
                response += "#" + first.get("dashboardURL").asText();
            }
        } catch (Exception e) {
            response = "Error parsing payload: " + e.getMessage();
        }

        SseEmitter latestEm = emitterController.getLatestEmitter();

        try {
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

    @PostMapping(
            value = "/demoone",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.TEXT_HTML_VALUE})
    ResponseEntity helloSample() {
        for (int i = 0; i < 30; i++) {
            WorkflowStub stub =
                    client.newUntypedWorkflowStub(
                            "DemoOneWorkflow",
                            WorkflowOptions.newBuilder()
                                    .setTaskQueue("DemoTaskQueue")
                                    .setWorkflowId("TestOneRun" + i)
                                    .build());

            stub.start();
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // pick one and wait for its result....
        WorkflowStub retStub = client.newUntypedWorkflowStub("TestOneRun1");
        // block till done....
        retStub.getResult(String.class);
        return new ResponseEntity<>("\"Test One Completed....\"", HttpStatus.OK);
    }
}
