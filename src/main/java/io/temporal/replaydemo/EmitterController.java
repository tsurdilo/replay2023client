package io.temporal.replaydemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmitterController {
    private List<SseEmitter> emitters = new ArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public List<SseEmitter> getEmitters() {
        return emitters;
    }

    public SseEmitter getLatestEmitter() {
        return emitters.isEmpty() ? null : emitters.get(emitters.size() - 1);
    }
}
