package teamseven.echoeco.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminHomeController {
    @GetMapping("")
    public String home() {
        return "admin/home";
    }

    @GetMapping("/question")
    public String question() {
        return "admin/question";
    }
}
