package teamseven.echoeco.question.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.QuestionType;

@Data
@Builder
public class QuestionResponse {
    private Long id;
    private String name;
    private QuestionType questionType;
    private String head;
    private String body;
    private String answer;

    public static QuestionResponse fromEntity(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .name(question.getName())
                .questionType(question.getQuestionType())
                .head(question.getHead())
                .body(question.getBody())
                .answer(question.getAnswer())
                .build();
    }
}
