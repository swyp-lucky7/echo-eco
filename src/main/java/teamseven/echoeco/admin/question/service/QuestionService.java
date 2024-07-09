package teamseven.echoeco.admin.question.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import teamseven.echoeco.admin.question.domain.Question;
import teamseven.echoeco.admin.question.domain.dto.QuestionRequest;
import teamseven.echoeco.admin.question.domain.dto.QuestionResponse;
import teamseven.echoeco.admin.question.repository.QuestionRepository;
import teamseven.echoeco.user.domain.User;

import java.util.List;

@Service
@Repository
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public void save(Question question) {
        questionRepository.save(question);
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow();
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public List<Question> findByUser(User user) {
        return questionRepository.findByMakeUser_Id(user.getId());
    }

    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    @Transactional
    public Question update(Long id, QuestionRequest questionRequest) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        question.update(questionRequest);

        return question;
    }
}
