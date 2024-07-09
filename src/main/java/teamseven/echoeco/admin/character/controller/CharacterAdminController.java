package teamseven.echoeco.admin.character.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.character.domain.CharacterDetail;
import teamseven.echoeco.admin.character.domain.dto.CharacterDetailDto;
import teamseven.echoeco.admin.character.domain.dto.CharacterRequest;
import teamseven.echoeco.admin.character.service.CharacterDetailService;
import teamseven.echoeco.admin.character.service.CharacterService;
import teamseven.echoeco.config.ApiResponse;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/character")
public class CharacterAdminController {

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

    @PostMapping("/delete")
    @ResponseBody
    public ApiResponse<String> deleteCharacter(@RequestBody Map<String, Long> map) {
        characterService.delete(map.get("id"));
        return ApiResponse.success("ok");
    }

    // --------------------------------------------------------
    // ----------------------  detail -------------------------
    // --------------------------------------------------------
    @GetMapping("/{id}/detail")
    public String detailPage(@PathVariable Long id, Model model) {

        Character character = characterService.findOne(id);
        model.addAttribute("character", character);
        return "admin/character/detail";
    }

    @GetMapping("/detail")
    @ResponseBody
    public ApiResponse<CharacterDetailDto> detail(@RequestParam("id") Long id) {
        CharacterDetail characterDetail = characterDetailService.findById(id);
        return ApiResponse.success(CharacterDetailDto.fromDto(characterDetail));
    }

    @PostMapping("/detail/delete")
    @ResponseBody
    public ApiResponse<String> deleteDetail(@RequestBody Map<String, Long> map) {
        characterDetailService.delete(map.get("id"));
        return ApiResponse.success("ok");
    }

    @GetMapping("/detail/list")
    @ResponseBody
    public ApiResponse<List<CharacterDetailDto>> detailListPage(@RequestParam("id") Long id) {
        List<CharacterDetail> list = characterDetailService.findByCharacterId(id);
        List<CharacterDetailDto> dtoList = list.stream().map(CharacterDetailDto::fromDto).toList();
        return ApiResponse.success(dtoList);
    }

    @PostMapping("/detail/create")
    @ResponseBody
    public ApiResponse<String> createDetail(@Valid @RequestBody CharacterDetailDto characterDetailRequest) {
        CharacterDetail characterDetail = characterDetailRequest.toEntity();
        characterDetailService.save(characterDetail);
        return ApiResponse.success("ok");
    }
}
