package teamseven.echoeco.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.video.domain.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
