package teamseven.echoeco.question.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.config.exception.NoRemainQuestionException;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.QuestionUserCount;
import teamseven.echoeco.question.domain.dto.QuestionCountDto;
import teamseven.echoeco.question.domain.dto.QuestionPostDto;
import teamseven.echoeco.question.domain.dto.QuestionRequest;
import teamseven.echoeco.question.domain.dto.QuestionResponse;
import teamseven.echoeco.question.repository.QuestionRepository;
import teamseven.echoeco.question.repository.QuestionUserCountRepository;
import teamseven.echoeco.user.domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionUserCountRepository questionUserCountRepository;

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

    public QuestionResponse question(User user) throws NoRemainQuestionException {
        QuestionUserCount questionUserCount = questionUserCountRepository.findByUser_Id(user.getId());

        LocalDate updateDate = questionUserCount.getUpdatedAt();
        LocalDate today = LocalDate.now();

        if (!updateDate.isEqual(today)) {
            questionUserCount.reset();
        }

        if (questionUserCount.getRemainCount() == 0) {
            throw new NoRemainQuestionException();
        }

        Long beforeQuestion = questionUserCount.getBeforeQuestion();
        List<Question> questionList;
        if (beforeQuestion != null) {
            questionList = questionRepository.findByIdNot(beforeQuestion);
        }
        else {
            questionList = questionRepository.findAll();
        }

        Random random = new Random();
        int randomIndex = random.nextInt(questionList.size());
        Question question = questionList.get(randomIndex);

        Long newBeforeQuestion = question.getId();
        questionUserCount.update(newBeforeQuestion);

        return QuestionResponse.fromEntity(question);
    }

    public QuestionPostDto questionPost(Long id, String select) {
        Question question = questionRepository.findById(id).orElseThrow();

        String answer;
        if (select.equals(question.getAnswer())) {
            answer = "CORRECT";
        }
        else {
            answer = "INCORRECT";
        }

        return QuestionPostDto.makeQuestionPostDto(answer);
    }

    public QuestionCountDto questionCount(User user) {
        QuestionUserCount questionUserCount = questionUserCountRepository.findByUser_Id(user.getId());
        int count = questionUserCount.getRemainCount();

        return QuestionCountDto.makeQuestionCountDto(count);
    }
}
