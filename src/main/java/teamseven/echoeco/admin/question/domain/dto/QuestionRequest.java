package teamseven.echoeco.admin.question.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import teamseven.echoeco.admin.question.domain.Question;
import teamseven.echoeco.admin.question.domain.QuestionType;
import teamseven.echoeco.user.User;

@Data
@Getter
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
                .answer(this.body)
                .makeUser(user)
                .build();
    }
}
