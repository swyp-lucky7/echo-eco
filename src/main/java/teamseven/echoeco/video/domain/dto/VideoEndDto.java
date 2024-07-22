package teamseven.echoeco.video.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoEndDto {
    private boolean isWatched;

    public static VideoEndDto makeVideoEndDto() {
        return VideoEndDto.builder()
                .isWatched(true)
                .build();
    }
}
