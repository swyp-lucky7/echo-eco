package teamseven.echoeco.question.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.question.domain.Question;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    public List<Question> findByMakeUser_Id(Long id);

    @Query("SELECT q.id FROM Question q")
    List<Long> findAllIds();
}
