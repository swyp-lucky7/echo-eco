package teamseven.echoeco.admin.home.controller;

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

    //GetMapping 으로 /question 만들어서 진행해주세요
    // todo 다빈님
    @GetMapping("")
    public String question() {
        return "admin/question";
    }
}
