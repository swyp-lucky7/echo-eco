package teamseven.echoeco.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.question.domain.QuestionUserCount;

import java.util.Optional;

public interface QuestionUserCountRepository extends JpaRepository<QuestionUserCount, Long> {
    Optional<QuestionUserCount> findByUser_Id(Long id);
}
