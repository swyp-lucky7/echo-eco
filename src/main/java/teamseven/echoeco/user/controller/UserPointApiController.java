package teamseven.echoeco.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.user.domain.Dto.UserPointRequest;
import teamseven.echoeco.user.domain.Dto.UserPointResponse;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.service.UserPointService;
import teamseven.echoeco.user.service.UserService;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class UserPointApiController {
    private final UserPointService userPointService;
    private final UserService userService;

    @PostMapping("/user/point")
    public ApiResponse<UserPointResponse> addUserPoint(Authentication authentication, @Valid @RequestBody UserPointRequest userPointRequest) {
        User user = userService.getUser(authentication);
        UserPoint userPoint = userPointService.addUserPoint(user, userPointRequest.getUserPoint());
        return ApiResponse.success(UserPointResponse.fromEntity(userPoint));
    }

    @GetMapping("/user/point")
    public ApiResponse<UserPointResponse> getUserPoint(Authentication authentication) {
        User user = userService.getUser(authentication);
        UserPoint userPoint = userPointService.findByUser(user);
        return ApiResponse.success(UserPointResponse.fromEntity(userPoint));
    }
}
