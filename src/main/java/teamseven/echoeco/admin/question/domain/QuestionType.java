package teamseven.echoeco.admin.question.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QuestionType {
    MULTIPLE_CHOICE("multipleChoice", "객관식"),
    SUBJECTIVE("subjective", "주관식");

    private final String name;
    private final String koreanName;
}
