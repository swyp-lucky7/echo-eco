package teamseven.echoeco.question.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.config.exception.NoRemainQuestionException;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.QuestionResultStatus;
import teamseven.echoeco.question.domain.ContentUserCount;
import teamseven.echoeco.question.domain.dto.*;
import teamseven.echoeco.question.repository.QuestionRepository;
import teamseven.echoeco.question.repository.ContentUserCountRepository;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.service.UserPointService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ContentUserCountRepository contentUserCountRepository;
    private final UserPointService userPointService;

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
        Optional<ContentUserCount> contentCountOptional = contentUserCountRepository.findByUser_Id(user.getId());
        ContentUserCount contentUserCount = contentCountOptional.orElseGet(() -> ContentUserCount.create(user));

        if (!contentUserCount.getResetAt().equals(LocalDate.now())) {
            contentUserCount.reset();
        }

        if (contentUserCount.getRemainQuestionCount() <= 0) {
            throw new NoRemainQuestionException();
        }

        List<Long> allIds = questionRepository.findAllIds();
        Long randomIndex = getRandomIndex(allIds, contentUserCount.getBeforeQuestion());

        Optional<Question> questionOptional = questionRepository.findById(randomIndex);
        Question question = questionOptional.orElseThrow(() -> new IllegalArgumentException("문제의 로직에 버그가 존재합니다."));
        contentUserCount.update(question.getId());

        contentUserCountRepository.save(contentUserCount);
        return QuestionResponse.fromEntity(question);
    }

    private Long getRandomIndex(List<Long> ids, Long beforeQuestion) {
        if (ids.size() == 1) {
            return ids.get(0);
        }
        Random random = new Random();
        Long randomId;
        do {
            randomId = ids.get(random.nextInt(ids.size()));
        } while (randomId.equals(beforeQuestion));
        return randomId;
    }

    public QuestionPostDto questionPost(User user, Long id, String select) throws NoRemainQuestionException {
        Optional<ContentUserCount> userCountOptional = contentUserCountRepository.findByUser_Id(user.getId());
        ContentUserCount contentUserCount = userCountOptional.orElseThrow(() -> new IllegalCallerException("문제를 낸 적 없는 유저입니다."));

        if (contentUserCount.getRemainQuestionCount() <= 0) {
            throw new NoRemainQuestionException();
        }
        contentUserCount.subtractRemainQuestionCount();

        Question question = questionRepository.findById(id).orElseThrow();
        QuestionResultStatus questionResultStatus = question.isCorrect(select);

        if (questionResultStatus.equals(QuestionResultStatus.CORRECT)) {
            int addQuestionPoint = 10;
            userPointService.addUserPoint(user, addQuestionPoint);
        }

        return QuestionPostDto.makeQuestionPostDto(questionResultStatus.name());
    }

    public ContentsRemainDto contentsRemain(User user) {
        Optional<ContentUserCount> questionUserCountOptional = contentUserCountRepository.findByUser_Id(user.getId());
        ContentUserCount contentUserCount = questionUserCountOptional.orElseGet(() -> ContentUserCount.create(user));
        if (!contentUserCount.getResetAt().equals(LocalDate.now())) {
            contentUserCount.reset();
        }

        return ContentsRemainDto.fromEntity(contentUserCount);
    }
}
