package teamseven.echoeco.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamseven.echoeco.config.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionApiController {

    @GetMapping("")
    public ApiResponse<String> read() {
        return null;
    }
}
