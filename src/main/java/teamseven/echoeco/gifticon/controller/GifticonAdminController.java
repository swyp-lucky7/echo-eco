package teamseven.echoeco.gifticon.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.gifticon.domain.dto.GifticonAdminRequest;
import teamseven.echoeco.gifticon.domain.dto.GifticonAdminResponse;
import teamseven.echoeco.gifticon.domain.dto.GifticonAdminSendRequest;
import teamseven.echoeco.gifticon.service.GifticonService;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/gifticon")
public class GifticonAdminController {
    private final GifticonService gifticonService;
    private final UserService userService;

    @GetMapping("")
    public String read() {
        return "admin/gifticon/read";
    }

    @GetMapping("/search")
    @ResponseBody
    public ApiResponse<List<GifticonAdminResponse>> search(@ModelAttribute GifticonAdminRequest gifticonAdminRequest) {
        return ApiResponse.success(gifticonService.search(gifticonAdminRequest.getUserEmail(), gifticonAdminRequest.getIsSend()));
    }

    @PostMapping("/send")
    @ResponseBody
    public ApiResponse<String> send(@Valid @RequestBody GifticonAdminSendRequest request,
                                    Authentication authentication) {
        User admin = userService.getUser(authentication);
        gifticonService.send(request, admin);
        return ApiResponse.success("ok");
    }
}
