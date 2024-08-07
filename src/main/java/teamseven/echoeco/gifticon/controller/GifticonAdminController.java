package teamseven.echoeco.gifticon.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.gifticon.domain.dto.GifticonDetailResponse;
import teamseven.echoeco.gifticon.domain.dto.GifticonUserAdminRequest;
import teamseven.echoeco.gifticon.domain.dto.GifticonUserAdminResponse;
import teamseven.echoeco.gifticon.service.GifticonService;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/gifticon")
public class GifticonAdminController {
    private final GifticonService gifticonService;
    private final UserService userService;

    @GetMapping("")
    public String read(Model model) {
        return "admin/gifticon/read";
    }

    @GetMapping("/search")
    @ResponseBody
    public ApiResponse<List<GifticonUserAdminResponse>> search(@ModelAttribute GifticonUserAdminRequest gifticonUserAdminRequest) {
        return ApiResponse.success(gifticonService.search(gifticonUserAdminRequest.getUserEmail(), gifticonUserAdminRequest.getIsSend()));
    }

    @PostMapping("/send")
    @ResponseBody
    public ApiResponse<String> send(@RequestParam("id") Long id,
                                    @RequestPart("file") MultipartFile file,
                                    Authentication authentication) throws MessagingException {
        User admin = userService.getUser(authentication);
        gifticonService.send(id, admin, file);
        return ApiResponse.success("ok");
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {
        GifticonDetailResponse detail = gifticonService.detail(id);
        model.addAttribute("detail", detail);
        return "admin/gifticon/detail";
    }
}
