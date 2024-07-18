package teamseven.echoeco.character.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.dto.*;
import teamseven.echoeco.character.service.CharacterService;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.user.service.UserService;
import teamseven.echoeco.util.GetUserEmail;

import java.util.List;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class CharacterApiController {
    private final CharacterService characterService;
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/character/list")
    public ApiResponse<List<CharacterPickListDto>> characterPickList(@RequestParam(required = false, name = "isPossible") Boolean isPossible) {
        return ApiResponse.success(characterService.pickList(isPossible));
    }

    @PostMapping("/character/pick")
    public ApiResponse<CharacterResponse> characterPick(@Valid @RequestBody CharacterPickRequest characterPickRequest, Authentication authentication) throws Exception {
        String userEmail = GetUserEmail.get(authentication);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("잘못된 유저 이메일 입니다."));
        Character pick = characterService.pick(characterPickRequest.getCharacterId(), user);
        return ApiResponse.success(CharacterResponse.fromEntity(pick));
    }

    @GetMapping("/character/user")
    public ApiResponse<CharacterUserResponse> characterUser(Authentication authentication) throws NotFoundCharacterUserException, NotAdminSettingException {
        User user = userService.getUser(authentication);
        return ApiResponse.success(characterService.characterUser(user));
    }

    @GetMapping("/character/complete/messages")
    public ApiResponse<CharacterCompleteMessagesDto> completeMessages(Authentication authentication) throws NotFoundCharacterUserException {
        String userEmail = GetUserEmail.get(authentication);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("잘못된 유저 이메일 입니다."));
        return ApiResponse.success(characterService.completeMessages(user));
    }

    @GetMapping("/character/trash")
    public ApiResponse<CharacterTrashDto> characterTrash(Authentication authentication) throws NotAdminSettingException, NotFoundCharacterUserException {
        User user = userService.getUser(authentication);
        return ApiResponse.success(characterService.characterTrash(user));
    }
}
