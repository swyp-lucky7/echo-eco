package teamseven.echoeco.video.domain;

import jakarta.persistence.*;
import lombok.*;
import teamseven.echoeco.video.domain.dto.VideoRequest;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String url;

    public void update(VideoRequest videoRequest) {
        this.name = videoRequest.getName();
        this.url = videoRequest.getUrl();
    }
}
