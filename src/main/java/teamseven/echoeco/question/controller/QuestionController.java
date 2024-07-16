package teamseven.echoeco.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.dto.QuestionRequest;
import teamseven.echoeco.question.domain.dto.QuestionResponse;
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


    @GetMapping("")
    public String readPage(Model model) {
        return "admin/question/read";
    }

    @GetMapping("/list")
    @ResponseBody
    public ApiResponse<List<Question>> list() {
        return ApiResponse.success(questionService.findAll());
    }

//    @GetMapping("")
//    public String read(Model model) {
//        List<QuestionResponse> questionResponse = questionService.findAll()
//                .stream()
//                .map(QuestionResponse::new)
//                .toList();
//        model.addAttribute("questionResponse", questionResponse);
//
//        return "admin/question/read";
//    }

//    @GetMapping("/read/{id}")
//    public ResponseEntity<QuestionResponse> readEach(@PathVariable("id") Long id) {
//        Question question = questionService.findById(id);
//
//        return ResponseEntity.ok()
//                .body(new QuestionResponse(question));
//    }

    @PostMapping("/delete")
    @ResponseBody
    public ApiResponse<String> delete(@RequestBody Map<String, Long> map) {
        questionService.delete(map.get("id"));
        return ApiResponse.success("ok");
    }
}
