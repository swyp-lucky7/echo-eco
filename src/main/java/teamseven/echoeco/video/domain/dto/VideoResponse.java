package teamseven.echoeco.video.domain.dto;

import lombok.Getter;
import teamseven.echoeco.video.domain.Video;

@Getter
public class VideoResponse {

    private final String name;
    private final String url;

    public VideoResponse(Video video) {
        this.name = video.getName();
        this.url = video.getUrl();
    }
}
