package teamseven.echoeco.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.question.domain.QuestionUserCount;

public interface QuestionUserCountRepository extends JpaRepository<QuestionUserCount, Long> {
    public QuestionUserCount findByUser_Id(Long id);
}
