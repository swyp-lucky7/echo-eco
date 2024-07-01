package teamseven.echoeco.admin.character.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.character.domain.CharacterDetail;
import teamseven.echoeco.admin.character.domain.dto.CharacterDetailRequest;
import teamseven.echoeco.admin.character.domain.dto.CharacterRequest;
import teamseven.echoeco.admin.character.service.CharacterDetailService;
import teamseven.echoeco.admin.character.service.CharacterService;
import teamseven.echoeco.config.ApiResponse;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/character")
public class CharacterController {

    private final CharacterService characterService;
    private final CharacterDetailService characterDetailService;

    @GetMapping("")
    public String readPage(Model model) {
        return "admin/character/read";
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiResponse<List<Character>> list() {
        return ApiResponse.success(characterService.findAll());
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("character", Character.empty());
        return "admin/character/create";
    }

    @GetMapping("/create/{id}")
    public String updatePage(@PathVariable Long id, Model model) {
        Character character = characterService.findOne(id);
        model.addAttribute("character", character);
        return "admin/character/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<String> upsert(@Valid @RequestBody CharacterRequest characterRequest) {
        Character character = characterRequest.toEntity();
        characterService.save(character);
        return ApiResponse.success("ok");
    }


    @GetMapping("/{id}/detail")
    public String detailPage(@PathVariable Long id, Model model) {
        Character character = characterService.findOne(id);
        model.addAttribute("character", character);
        return "admin/character/detail";
    }

    @PostMapping("/create/detail")
    @ResponseBody
    public ApiResponse<String> createDetail(@Valid @RequestBody CharacterDetailRequest characterDetailRequest) {
        CharacterDetail characterDetail = characterDetailRequest.toEntity();
        characterDetailService.save(characterDetail);
        return ApiResponse.success("ok");
    }
}
