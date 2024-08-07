package teamseven.echoeco.trash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.exception.AlreadyCleanTrashException;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.trash.domain.dto.TrashStatusDto;
import teamseven.echoeco.trash.service.TrashUserService;
import teamseven.echoeco.user.domain.Dto.UserPointDto;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.service.UserService;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class TrashUserController {
    private final TrashUserService trashUserService;
    private final UserService userService;

    @PostMapping("/trash/clear")
    public ApiResponse<UserPointDto> clearTrash(Authentication authentication) throws AlreadyCleanTrashException, NotAdminSettingException, NotFoundCharacterUserException {
        User user = userService.getUser(authentication);
        return ApiResponse.success(trashUserService.cleanTrash(user));
    }

    @GetMapping("/trash/status")
    public ApiResponse<TrashStatusDto> trashStatus(Authentication authentication) {
        User user = userService.getUser(authentication);
        return ApiResponse.success(trashUserService.trashStatus(user));
    }
}
