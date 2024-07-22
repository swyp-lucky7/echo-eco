package teamseven.echoeco.video.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.video.domain.Video;

@Data
@Builder
public class VideoResponse {
    private Long id;
    private String name;
    private String url;

    public static VideoResponse fromEntity(Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .name(video.getName())
                .url(video.getUrl())
                .build();
    }
}
