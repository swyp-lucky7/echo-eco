package teamseven.echoeco.question.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionPostRequest {
    @NotNull
    private Long id;
    @NotNull
    private String select;
}
