package teamseven.echoeco.question.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.QuestionType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {
    @NotNull
    private String name;
    @NotNull
    private QuestionType questionType;
    @NotNull
    private String head;
    private String body;
    @NotNull
    private String answer;

    public Question toEntity() {
        return Question.builder()
                .name(this.name)
                .questionType(this.questionType)
                .head(this.head)
                .body(this.body)
                .answer(this.answer)
                .build();
    }
}
