package teamseven.echoeco.question.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionResultStatus {
    CORRECT("correct", "정답"),
    INCORRECT("incorrect", "오답");

    private final String key;
    private final String koreaName;
}
