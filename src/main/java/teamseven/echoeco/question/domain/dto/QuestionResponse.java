package teamseven.echoeco.admin.question.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import teamseven.echoeco.admin.question.domain.Question;

@Getter
public class QuestionResponse {

    @NotNull
    private Long id;
    @NotNull
    private String head;
    @NotNull
    private String body;
    @NotNull
    private String answer;

    public QuestionResponse(Question entity) {
        this.id = entity.getId();
        this.head = entity.getHead();
        this.body = entity.getBody();
        this.answer = entity.getAnswer();
    }
}
