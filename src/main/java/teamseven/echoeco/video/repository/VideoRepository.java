package teamseven.echoeco.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import teamseven.echoeco.video.domain.Video;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("SELECT v.id FROM Video v")
    List<Long> findAllIds();
}
