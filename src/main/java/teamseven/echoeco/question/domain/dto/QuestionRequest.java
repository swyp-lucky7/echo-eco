package teamseven.echoeco.question.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.QuestionType;
import teamseven.echoeco.user.domain.User;

@Data
@Builder
public class QuestionRequest {
    @NotNull
    private String name;
    @NotNull
    private QuestionType questionType;
    @NotNull
    private String head;
    @NotNull
    private String body;
    @NotNull
    private String answer;

    public Question toEntity(User user) {
        return Question.builder()
                .name(this.name)
                .questionType(this.questionType)
                .head(this.head)
                .body(this.body)
                .answer(this.answer)
                .makeUser(user)
                .build();
    }
}
