package teamseven.echoeco.login.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class LoginController {

    @GetMapping("/")
    public String index() {
        return "login/index";
    }

    @GetMapping("/user/login")
    public String login() {
        return "login/loginPage";
    }

    @GetMapping("/token/init")
    public String tokenInit() {
        return "login/tokenInit";
    }
}
