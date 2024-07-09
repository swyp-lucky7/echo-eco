package teamseven.echoeco.admin.character.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import teamseven.echoeco.admin.background.service.BackgroundService;
import teamseven.echoeco.admin.character.domain.CharacterType;
import teamseven.echoeco.admin.character.domain.dto.CharacterPickListCondition;
import teamseven.echoeco.admin.character.domain.dto.CharacterResponse;
import teamseven.echoeco.admin.character.service.CharacterDetailService;
import teamseven.echoeco.admin.character.service.CharacterService;
import teamseven.echoeco.config.ApiResponse;

import java.util.List;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class CharacterApiController {
    private final CharacterService characterService;
    private final CharacterDetailService characterDetailService;
    private final BackgroundService backgroundService;

    @GetMapping("/character/list")
    public ApiResponse<List<CharacterResponse>> characterPickList(@RequestParam(required = false) CharacterType type, @RequestParam(required = false) Boolean isPossible) {
        return ApiResponse.success(characterService.pickList(type, isPossible));
    }
}
