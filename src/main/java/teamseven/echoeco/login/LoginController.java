package teamseven.echoeco.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import teamseven.echoeco.config.auth.LoginUser;
import teamseven.echoeco.user.User;

@RequiredArgsConstructor
@Controller
public class LoginController {

    @GetMapping("/")
    public String index(Model model, @LoginUser User user) {
        return "index";
    }

    @GetMapping("/user/login")
    public String login(Model model) {
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

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
