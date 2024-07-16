package teamseven.echoeco.video.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import teamseven.echoeco.video.domain.Video;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoRequest {
    @NotNull
    private String name;
    @NotNull
    private String url;

    public Video toEntity() {
        return Video.builder()
                .name(this.name)
                .url(this.url)
                .build();
    }
}
