package io.temporal.replaydemo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClientController {
    private final List<SseEmitter> emitters = new ArrayList<>();

    @GetMapping("/")
    public String toIndex(Model model) {
        return "index";
    }

}
