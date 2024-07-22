package teamseven.echoeco.question.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionPostDto {
    private String answer;

    public static QuestionPostDto makeQuestionPostDto(String answer) {
        return QuestionPostDto.builder()
                .answer(answer)
                .build();
    }
}
