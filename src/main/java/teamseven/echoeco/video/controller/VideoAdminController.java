package teamseven.echoeco.video.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.video.domain.Video;
import teamseven.echoeco.video.domain.dto.VideoRequest;
import teamseven.echoeco.video.service.VideoService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/video")
public class VideoAdminController {

    private final VideoService videoService;

    @GetMapping("")
    public String readPage(Model model) {
        return "admin/video/read";
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiResponse<List<Video>> list() {
        return ApiResponse.success(videoService.findAll());
    }

    @GetMapping("/create")
    public String createPage() {
        return "admin/video/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<String> create(@Valid @RequestBody VideoRequest videoRequest) {
        Video video = videoRequest.toEntity();
        videoService.save(video);
        return ApiResponse.success("ok");
    }

    @GetMapping("/create/{id}")
    public String updatePage(@PathVariable(value = "id") Long id, Model model) {
        Video video = videoService.findById(id);
        model.addAttribute("video", video);
        return "admin/video/update";
    }

    @PostMapping("/create/{id}")
    @ResponseBody
    public ApiResponse<String> update(@PathVariable(value = "id") Long id,
                                      @RequestBody VideoRequest videoRequest) {
        videoService.update(id, videoRequest);
        return ApiResponse.success("ok");
    }

    @PostMapping("/delete")
    @ResponseBody
    public ApiResponse<String> delete(@RequestBody Map<String, Long> map) {
        videoService.delete(map.get("id"));
        return ApiResponse.success("ok");
    }
}
