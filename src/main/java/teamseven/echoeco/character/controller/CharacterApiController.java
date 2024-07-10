package teamseven.echoeco.character.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.background.domain.Background;
import teamseven.echoeco.background.service.BackgroundService;
import teamseven.echoeco.character.domain.*;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.dto.CharacterPickRequest;
import teamseven.echoeco.character.domain.dto.CharacterResponse;
import teamseven.echoeco.character.domain.dto.CharacterUserResponse;
import teamseven.echoeco.character.service.CharacterDetailService;
import teamseven.echoeco.character.service.CharacterService;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.util.GetUserEmail;

import java.util.List;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class CharacterApiController {
    private final CharacterService characterService;
    private final CharacterDetailService characterDetailService;
    private final BackgroundService backgroundService;
    private final UserRepository userRepository;

    @GetMapping("/character/list")
    public ApiResponse<List<CharacterResponse>> characterPickList(@RequestParam(required = false, defaultValue = "ALL", name = "type") CharacterType type,
                                                                  @RequestParam(required = false, name = "isPossible") Boolean isPossible) {
        return ApiResponse.success(characterService.pickList(type, isPossible));
    }

    @PostMapping("/character/pick")
    public ApiResponse<CharacterResponse> characterPick(@Valid @RequestBody CharacterPickRequest characterPickRequest, Authentication authentication) throws Exception {
        String userEmail = GetUserEmail.get(authentication);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("잘못된 유저 이메일 입니다."));
        Character pick = characterService.pick(characterPickRequest.getCharacterId(), user);
        return ApiResponse.success(CharacterResponse.fromEntity(pick));
    }

    @GetMapping("/character/user")
    public ApiResponse<CharacterUserResponse> characterUserResponse(Authentication authentication) throws NotFoundCharacterUserException, NotAdminSettingException {
        String userEmail = GetUserEmail.get(authentication);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("잘못된 유저 이메일 입니다."));
        return ApiResponse.success(characterService.characterUser(user));
    }
}
