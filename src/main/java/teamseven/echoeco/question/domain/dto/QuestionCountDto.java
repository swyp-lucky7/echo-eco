package teamseven.echoeco.question.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionCountDto {
    private int count;

    public static QuestionCountDto makeQuestionCountDto(int count) {
        return QuestionCountDto.builder()
                .count(count)
                .build();
    }
}
