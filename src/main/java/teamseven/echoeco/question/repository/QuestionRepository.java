package teamseven.echoeco.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.question.domain.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByMakeUser_Id(Long id);

    @Query("SELECT q.id FROM Question q")
    List<Long> findAllIds();
}
