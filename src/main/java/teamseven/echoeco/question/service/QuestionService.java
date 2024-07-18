package teamseven.echoeco.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.dto.QuestionRequest;
import teamseven.echoeco.question.repository.QuestionRepository;
import teamseven.echoeco.user.domain.User;

import java.util.List;

@Service
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

    public Question update(Long id, QuestionRequest questionRequest) {
        Question question = questionRepository.findById(id).orElseThrow();
        question.update(questionRequest);
        save(question);
        return question;
    }
}
