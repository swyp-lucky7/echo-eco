package teamseven.echoeco.admin.question.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionType {
    MULTIPLE_CHOICE("multipleChoice", "객관식"),
    SUBJECTIVE("subjective", "주관식");

    private final String name;
    private final String koreanName;
}
