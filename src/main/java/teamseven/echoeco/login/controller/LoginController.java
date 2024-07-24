package teamseven.echoeco.login.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String tokenInit(@RequestParam(name = "token") String jwtToken,
                            HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization", jwtToken);
        cookie.setHttpOnly(true);  // 자바스크립트에서 접근 불가
//        cookie.setSecure(true);  // HTTPS에서만 전송
//        cookie.setDomain(".abc.com");
        cookie.setPath("/admin");
        cookie.setMaxAge(24 * 60 * 60);  // 24 시간

        response.addCookie(cookie);

        return "login/tokenInit";
    }
}
