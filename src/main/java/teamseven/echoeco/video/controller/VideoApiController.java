package teamseven.echoeco.video.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.service.UserService;
import teamseven.echoeco.video.domain.dto.VideoEndDto;
import teamseven.echoeco.video.domain.dto.VideoResponse;
import teamseven.echoeco.video.service.VideoService;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class VideoApiController {
    private final VideoService videoService;
    private final UserService userService;

    @GetMapping("/video")
    public ApiResponse<VideoResponse> video() {
        return ApiResponse.success(videoService.video());
    }

    @GetMapping("/video/end")
    public ApiResponse<VideoEndDto> videoEnd(Authentication authentication) {
        User user = userService.getUser(authentication);
        return ApiResponse.success(videoService.videoEnd(user));
    }

}
