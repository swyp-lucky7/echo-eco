package teamseven.echoeco.trash.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.character.domain.dto.CharacterUserResponse;
import teamseven.echoeco.character.service.CharacterService;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.exception.AlreadyCleanTrashException;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.trash.service.TrashUserService;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.util.GetUserEmail;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class TrashUserController {
    private final TrashUserService trashUserService;
    private final UserRepository userRepository;
    private final CharacterService characterService;

    @PostMapping("/trash/clear")
    public ApiResponse<CharacterUserResponse> clearTrash(Authentication authentication) throws AlreadyCleanTrashException, NotAdminSettingException, NotFoundCharacterUserException {
        String userEmail = GetUserEmail.get(authentication);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("잘못된 유저 이메일 입니다."));

        trashUserService.cleanTrash(user);
        return ApiResponse.success(characterService.characterUser(user));
    }
}
