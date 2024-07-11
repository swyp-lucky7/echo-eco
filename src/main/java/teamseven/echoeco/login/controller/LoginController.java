package teamseven.echoeco.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.user.domain.OAuth2.CustomOauth2User;
import teamseven.echoeco.user.repository.UserRepository;

@RequiredArgsConstructor
@Controller
@Slf4j
public class LoginController {
    private final UserRepository repository;

    @GetMapping("/")
    public String index(Model model) {

        return "login/index";
    }

    @GetMapping("/user/login")
    public String login() {
        return "login/loginPage";
    }

    @ResponseBody()
    @GetMapping("/my")
    public String admin(HttpServletRequest request) {
        return "admin";
    }

    @ResponseBody()
    @GetMapping("/my2")
    public String user(HttpServletRequest request) {
        return "user";
    }

    @ResponseBody()
    @GetMapping("/test")
    public String testest(Authentication authentication) {

        CustomOauth2User principal = (CustomOauth2User) authentication.getPrincipal();
        return principal.getEmail();
    }
}
