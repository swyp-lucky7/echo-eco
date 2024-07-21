package teamseven.echoeco.question.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.config.exception.NoRemainQuestionException;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.QuestionResultStatus;
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
import java.util.Optional;
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
        Optional<QuestionUserCount> questionUserCountOptional = questionUserCountRepository.findByUser_Id(user.getId());
        QuestionUserCount questionUserCount = questionUserCountOptional.orElseGet(QuestionUserCount::create);

        if (questionUserCount.getUpdatedAt().equals(LocalDate.now())) {
            questionUserCount.reset();
        }

        if (questionUserCount.getRemainCount() == 0) {
            throw new NoRemainQuestionException();
        }

        long count = questionRepository.count();
        Long randomIndex = getRandomIndex(count, questionUserCount.getBeforeQuestion());

        Optional<Question> questionOptional = questionRepository.findById(randomIndex);
        Question question = questionOptional.orElseThrow();
        questionUserCount.update(question.getId());

        questionUserCountRepository.save(questionUserCount);
        return QuestionResponse.fromEntity(question);
    }

    private Long getRandomIndex(long size, Long beforeQuestion) {
        Random random = new Random();
        long randomId;
        do {
            randomId = random.nextLong(size);
        } while (randomId != beforeQuestion);
        return randomId;
    }

    public QuestionPostDto questionPost(Long id, String select) {
        Question question = questionRepository.findById(id).orElseThrow();

        QuestionResultStatus questionResultStatus = question.isCorrect(select);
        return QuestionPostDto.makeQuestionPostDto(questionResultStatus.name());
    }

    public QuestionCountDto questionCount(User user) {
        Optional<QuestionUserCount> questionUserCountOptional = questionUserCountRepository.findByUser_Id(user.getId());
        QuestionUserCount questionUserCount = questionUserCountOptional.orElseGet(QuestionUserCount::create);
        int count = questionUserCount.getRemainCount();

        return QuestionCountDto.makeQuestionCountDto(count);
    }
}
