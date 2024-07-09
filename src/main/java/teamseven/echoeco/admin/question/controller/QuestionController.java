package teamseven.echoeco.admin.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.admin.question.domain.Question;
import teamseven.echoeco.admin.question.domain.dto.QuestionRequest;
import teamseven.echoeco.admin.question.service.QuestionService;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.util.GetUserEmail;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @GetMapping("")
    public String readPage(Model model ) {
        return "admin/question/read";
    }

    @GetMapping("/create")
    public String createPage() {
        return "admin/question/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<String> create(@Valid @RequestParam QuestionRequest questionRequest,
                                      Authentication authentication) {
        String userEmail = GetUserEmail.get(authentication);
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("잘못된 유저 이메일 입니다."));

        Question entity = questionRequest.toEntity(user);
        questionService.save(entity);
        return ApiResponse.success("ok");
    }
}
