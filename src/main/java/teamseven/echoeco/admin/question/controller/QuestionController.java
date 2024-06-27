package teamseven.echoeco.admin.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.admin.question.domain.Question;
import teamseven.echoeco.admin.question.domain.dto.QuestionRequest;
import teamseven.echoeco.admin.question.service.QuestionService;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.auth.LoginUser;
import teamseven.echoeco.user.User;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("")
    public String readPage(Model model, @LoginUser User user) {
        return "admin/question/read";
    }

    @GetMapping("/create")
    public String createPage() {
        return "admin/question/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<String> create(@Valid @RequestParam QuestionRequest questionRequest, @LoginUser User user) {
        Question entity = questionRequest.toEntity(user);
        questionService.save(entity);
        return ApiResponse.success("ok");
    }
}
