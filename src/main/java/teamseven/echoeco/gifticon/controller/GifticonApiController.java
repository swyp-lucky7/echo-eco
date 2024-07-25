package teamseven.echoeco.gifticon.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.gifticon.domain.dto.GifticonPostRequest;
import teamseven.echoeco.gifticon.service.GifticonService;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class GifticonApiController {
    private final GifticonService gifticonService;
    private final UserService userService;

    @PostMapping("/gifticon")
    public ApiResponse<String> post(@Valid @RequestBody GifticonPostRequest request,
                                    Authentication authentication) throws NotFoundCharacterUserException {
        User user = userService.getUser(authentication);
        gifticonService.post(request.getEmail(), user);
        return ApiResponse.success("ok");
    }

//    @GetMapping("/")
}
