package teamseven.echoeco.admin.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.admin.question.domain.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    public List<Question> findByMakeUser_Id(Long id);
}
