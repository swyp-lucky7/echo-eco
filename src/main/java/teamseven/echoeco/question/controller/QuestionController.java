package teamseven.echoeco.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.dto.QuestionRequest;
import teamseven.echoeco.question.service.QuestionService;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.util.GetUserEmail;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @GetMapping("")
    public String readPage(Model model) {
        return "admin/question/read";
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiResponse<List<Question>> list() { return ApiResponse.success(questionService.findAll()); }

    @GetMapping("/create")
    public String createPage() {
        return "admin/question/create";
    }

    @PostMapping("/create")
    @ResponseBody
    public ApiResponse<String> create(@Valid @RequestBody QuestionRequest questionRequest) {
        Question question = questionRequest.toEntity();
        questionService.save(question);
        return ApiResponse.success("ok");
    }

    @GetMapping("/create/{id}")
    public String updatePage(@PathVariable(value = "id") Long id, Model model) {
        Question question = questionService.findById(id);
        model.addAttribute("question", question);
        return "admin/question/update";
    }

    @PostMapping("/create/{id}")
    @ResponseBody
    public ApiResponse<String> update(@PathVariable(value = "id") Long id,
                                      @RequestBody QuestionRequest questionRequest) {
        questionService.update(id, questionRequest);
        return ApiResponse.success("ok");
    }

    @PostMapping("/delete")
    @ResponseBody
    public ApiResponse<String> delete(@RequestBody Map<String, Long> map) {
        questionService.delete(map.get("id"));
        return ApiResponse.success("ok");
    }
}
