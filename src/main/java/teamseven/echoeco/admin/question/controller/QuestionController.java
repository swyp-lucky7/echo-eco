package teamseven.echoeco.admin.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import teamseven.echoeco.config.auth.LoginUser;
import teamseven.echoeco.user.User;

@Controller("/admin/question")
@RequiredArgsConstructor
public class QuestionController {

    @GetMapping("")
    public String readPage(Model model, @LoginUser User user) {
        return "admin/question/read";
    }

    @GetMapping("/create")
    public String createPage() {
        return "admin/question/create";
    }
}
