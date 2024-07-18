package teamseven.echoeco.trash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.character.domain.dto.CharacterUserResponse;
import teamseven.echoeco.character.service.CharacterService;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.exception.AlreadyCleanTrashException;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.trash.domain.dto.TrashStatusDto;
import teamseven.echoeco.trash.service.TrashUserService;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.user.service.UserService;
import teamseven.echoeco.util.GetUserEmail;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class TrashUserController {
    private final TrashUserService trashUserService;
    private final UserRepository userRepository;
    private final CharacterService characterService;
    private final UserService userService;

    @PostMapping("/trash/clear")
    public ApiResponse<UserPoint> clearTrash(Authentication authentication) throws AlreadyCleanTrashException, NotAdminSettingException, NotFoundCharacterUserException {
        String userEmail = GetUserEmail.get(authentication);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("잘못된 유저 이메일 입니다."));

        UserPoint userPoint = trashUserService.cleanTrash(user);
        return ApiResponse.success(userPoint);
    }

    @GetMapping("/trash/status")
    public ApiResponse<TrashStatusDto> trashStatus(Authentication authentication) {
        User user = userService.getUser(authentication);
        return ApiResponse.success(trashUserService.trashStatus(user));
    }
}
