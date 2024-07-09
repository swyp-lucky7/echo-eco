package teamseven.echoeco.admin.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.question.domain.Question;
import teamseven.echoeco.admin.question.domain.dto.QuestionRequest;
import teamseven.echoeco.admin.question.domain.dto.QuestionResponse;
import teamseven.echoeco.admin.question.service.QuestionService;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.auth.LoginUser;
import teamseven.echoeco.user.domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/question")
public class QuestionController {

    private final QuestionService questionService;

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

    @GetMapping("")
    public String readPage() {
        return "admin/question/read";
    }

    @GetMapping("/read")
    public String read(Model model) {
        List<QuestionResponse> questionResponse = questionService.findAll()
                .stream()
                .map(QuestionResponse::new)
                .toList();
        model.addAttribute("questionResponse", questionResponse);

        return "read";
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<QuestionResponse> readEach(@PathVariable("id") Long id) {
        Question question = questionService.findById(id);

        return ResponseEntity.ok()
                .body(new QuestionResponse(question));
    }

    @DeleteMapping("/read/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        questionService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<Question> update(@PathVariable("id") Long id,
                                                 @RequestBody QuestionRequest questionRequest) {
        Question updateQuestion = questionService.update(id, questionRequest);

        return ResponseEntity.ok()
                .body(updateQuestion);
    }
}
