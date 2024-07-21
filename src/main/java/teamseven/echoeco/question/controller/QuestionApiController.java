package teamseven.echoeco.question.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import teamseven.echoeco.config.ApiResponse;
import teamseven.echoeco.config.exception.NoRemainQuestionException;
import teamseven.echoeco.question.domain.dto.QuestionCountDto;
import teamseven.echoeco.question.domain.dto.QuestionPostDto;
import teamseven.echoeco.question.domain.dto.QuestionPostRequest;
import teamseven.echoeco.question.domain.dto.QuestionResponse;
import teamseven.echoeco.question.service.QuestionService;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.service.UserService;

@Controller
@RequiredArgsConstructor
@ResponseBody
public class QuestionApiController {
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/question")
    public ApiResponse<QuestionResponse> question(Authentication authentication) throws NoRemainQuestionException {
        User user = userService.getUser(authentication);
        return ApiResponse.success(questionService.question(user));
    }

    @PostMapping("/question/post")
    public ApiResponse<QuestionPostDto> questionPost(@Valid @RequestBody QuestionPostRequest questionPostRequest) throws Exception {
        QuestionPostDto questionPostDto = questionService.questionPost(questionPostRequest.getId(), questionPostRequest.getSelect());
        return ApiResponse.success(questionPostDto);
    }

    @GetMapping("/question/count")
    public ApiResponse<QuestionCountDto> questionCount(Authentication authentication) {
        User user = userService.getUser(authentication);
        return ApiResponse.success(questionService.questionCount(user));
    }
}