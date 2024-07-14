package teamseven.echoeco.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.service.UserPointService;
import teamseven.echoeco.user.service.UserService;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class UserPointController {
    private final UserPointService userPointService;
    private final UserService userService;

    // 테스트를 위해 작성, UserPoint 증가 후 아이템 구매 테스트 진행하기 위함.
    @PostMapping("/item/point/add")
    public ApiResponse<UserPoint> addUserPoint(Authentication authentication, @RequestParam int add_point) {
        User user = userService.getUser(authentication);
        UserPoint userPoint = userPointService.addUserPoint(user, add_point);
        return ApiResponse.success(userPoint);
    }
}
