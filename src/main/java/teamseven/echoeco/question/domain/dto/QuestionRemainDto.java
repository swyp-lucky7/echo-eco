package teamseven.echoeco.question.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.question.domain.QuestionUserCount;

@Data
@Builder
public class QuestionRemainDto {
    public int remainQuestion;
    public int remainVideo;

    public static QuestionRemainDto makeQuestionRemainDto(QuestionUserCount questionUserCount) {
        return QuestionRemainDto.builder()
                .remainQuestion(questionUserCount.getRemainQuestionCount())
                .remainVideo(questionUserCount.getRemainVideoCount())
                .build();
    }
}
