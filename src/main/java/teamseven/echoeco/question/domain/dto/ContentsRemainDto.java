package teamseven.echoeco.question.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.question.domain.ContentUserCount;

@Data
@Builder
public class ContentsRemainDto {
    public int remainQuestion;
    public int remainVideo;

    public static ContentsRemainDto fromEntity(ContentUserCount contentUserCount) {
        return ContentsRemainDto.builder()
                .remainQuestion(contentUserCount.getRemainQuestionCount())
                .remainVideo(contentUserCount.getRemainVideoCount())
                .build();
    }
}
