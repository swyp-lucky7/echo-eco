package teamseven.echoeco.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import teamseven.echoeco.config.auth.LoginUser;
import teamseven.echoeco.user.SessionUser;

@RequiredArgsConstructor
@Controller
public class LoginController {


    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        if (user != null) {
            model.addAttribute("userName", user.getName());
            System.out.println(user.getRole().name());
        }

        return "index";
    }

    @GetMapping("/user/login")
    public String login(Model model) {

        System.out.println("test");
        return "loginPage";
    }

    @GetMapping("/user/test")
    public String test(Model model) {
        return "index";
    }

    @GetMapping("/rr")
    public String tt() {
        return "test";
    }
}
