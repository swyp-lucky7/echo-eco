package teamseven.echoeco.admin.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import teamseven.echoeco.admin.question.domain.Question;
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

    public QuestionResponse update(Long id) {
        Question entity = questionRepository.findById(id).orElseThrow();

        return new QuestionResponse(entity);
    }
}
