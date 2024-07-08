package teamseven.echoeco.admin.background.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.admin.background.domain.Background;
import teamseven.echoeco.admin.background.domain.dto.BackgroundDto;
import teamseven.echoeco.admin.background.service.BackgroundService;
import teamseven.echoeco.config.ApiResponse;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/background")
public class BackgroundController {

    private final BackgroundService backgroundService;

    @GetMapping("")
    public String readPage() {
        return "admin/background/read";
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiResponse<List<Background>> list() {
        return ApiResponse.success(backgroundService.findAll());
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("background", Background.empty());
        return "admin/background/create";
    }

    @GetMapping("/create/{id}")
    public String updatePage(@PathVariable Long id, Model model) {
        Background background = backgroundService.findById(id);
        model.addAttribute("background", background);
        return "admin/background/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<String> upsert(@Valid @RequestBody BackgroundDto backgroundDto) {
        Background entity = backgroundDto.toEntity();
        backgroundService.save(entity);
        return ApiResponse.success("ok");
    }

    @PostMapping("/delete")
    @ResponseBody
    public ApiResponse<String> delete(@RequestBody Map<String, Long> map) {
        Long id = map.get("id");
        backgroundService.deleteById(id);
        return ApiResponse.success("ok");
    }
}
