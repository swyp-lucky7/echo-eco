package teamseven.echoeco.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.jwt.JwtUtil;
import teamseven.echoeco.user.domain.Dto.UserCheckDto;
import teamseven.echoeco.user.domain.Dto.UserTokenUpdateResultDto;
import teamseven.echoeco.user.service.UserService;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class LoginApiController {
    private final UserService userService;
    private final JwtUtil jwtUtil;


    @GetMapping("/user/check")
    public ApiResponse<UserCheckDto> checkUser(HttpServletRequest request) {
        UserCheckDto userCheckDto = new UserCheckDto();
        userCheckDto.setCheck_status((boolean) request.getAttribute("userCheck"));
        userCheckDto.setDetail((String) request.getAttribute("userCheckDetail"));

        return ApiResponse.success(userCheckDto);
    }

    @GetMapping("/user/token/update")
    public ApiResponse<UserTokenUpdateResultDto> updateUserToken(HttpServletRequest request, HttpServletResponse response) {
        String token = (String) request.getAttribute("token");

        //ToDo
        //Refresh Token 구현 후 JWT 토큰 연장 로직 구현 필요.
//        String name = jwtUtil.getName(token);
//        String email = jwtUtil.getEmail(token);
//        String role = jwtUtil.getRole(token).name();
//        String newToken = jwtUtil.createJwt(name, role, email, 60 * 60 * 60L);
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals(TOKEN_NAME)) {
//                cookie.setValue(newToken);
//            }
//        }
        UserTokenUpdateResultDto userTokenUpdateResultDto = new UserTokenUpdateResultDto();
        userTokenUpdateResultDto.setUpdate_statue(true);
        return ApiResponse.success(userTokenUpdateResultDto);
    }
}
