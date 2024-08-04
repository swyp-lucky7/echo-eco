package teamseven.echoeco.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.file.service.FileService;


@RequiredArgsConstructor
@RequestMapping("/admin/file")
@RestController
public class S3Controller {
    private final FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file) {
        String url = fileService.upload(file);
        return ApiResponse.success(url);
    }

}
