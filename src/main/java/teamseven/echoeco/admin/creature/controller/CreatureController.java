package teamseven.echoeco.admin.creature.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.admin.creature.domain.Creature;
import teamseven.echoeco.admin.creature.domain.CreatureDetail;
import teamseven.echoeco.admin.creature.domain.dto.CreatureDetailRequest;
import teamseven.echoeco.admin.creature.domain.dto.CreatureRequest;
import teamseven.echoeco.admin.creature.service.CreatureDetailService;
import teamseven.echoeco.admin.creature.service.CreatureService;
import teamseven.echoeco.config.ApiResponse;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/creature")
public class CreatureController {

    private final CreatureService creatureService;
    private final CreatureDetailService creatureDetailService;

    @GetMapping("")
    public String readPage(Model model) {
        return "admin/creature/read";
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiResponse<List<Creature>> list() {
        return ApiResponse.success(creatureService.findAll());
    }

    @GetMapping("/create")
    public String createPage() {
        return "admin/creature/create";
    }

    @GetMapping("/create/{id}")
    public String updatePage(@PathVariable Long id, Model model) {
        Creature creature = creatureService.findOne(id);
        model.addAttribute("creature", creature);
        return "admin/creature/update";
    }

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<String> upsert(@Valid @RequestBody CreatureRequest creatureRequest) {
        Creature creature = creatureRequest.toEntity();
        creatureService.save(creature);
        return ApiResponse.success("ok");
    }


    @GetMapping("/{id}/detail")
    public String detailPage(@PathVariable Long id, Model model) {
        Creature creature = creatureService.findOne(id);
        model.addAttribute("creature", creature);
        return "admin/creature/detail";
    }

    @PostMapping("/create/detail")
    @ResponseBody
    public ApiResponse<String> createDetail(@Valid @RequestBody CreatureDetailRequest creatureDetailRequest) {
        CreatureDetail creatureDetail = creatureDetailRequest.toEntity();
        creatureDetailService.save(creatureDetail);
        return ApiResponse.success("ok");
    }
}
