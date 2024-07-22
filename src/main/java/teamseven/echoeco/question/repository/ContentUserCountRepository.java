package teamseven.echoeco.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.question.domain.ContentUserCount;

import java.util.Optional;

public interface ContentUserCountRepository extends JpaRepository<ContentUserCount, Long> {
    Optional<ContentUserCount> findByUser_Id(Long id);
}
