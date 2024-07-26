package teamseven.echoeco.question.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.config.exception.NoRemainQuestionException;
import teamseven.echoeco.question.domain.ContentUserCount;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.QuestionResultStatus;
import teamseven.echoeco.question.domain.QuestionType;
import teamseven.echoeco.question.domain.dto.ContentsRemainDto;
import teamseven.echoeco.question.domain.dto.QuestionPostDto;
import teamseven.echoeco.question.domain.dto.QuestionRequest;
import teamseven.echoeco.question.domain.dto.QuestionResponse;
import teamseven.echoeco.question.repository.QuestionRepository;
import teamseven.echoeco.question.repository.ContentUserCountRepository;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.user.service.UserPointService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class QuestionServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ContentUserCountRepository contentUserCountRepository;
    @Autowired
    private UserPointRepository userPointRepository;

    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        UserPointService userPointService = new UserPointService(userPointRepository);
        questionService = new QuestionService(questionRepository, contentUserCountRepository, userPointService);
    }

    @Test
    @DisplayName("question save 요청이 오면 저장이 되어야 한다.")
    public void givenQuestion_whenSaveQuestion_thenQuestionSaved() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question = Question.builder()
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .head("header")
                .name("hi")
                .body("body")
                .answer("1")
                .build();

        //when
        questionService.save(question);

        //then
        Question findQuestion = questionService.findById(question.getId());
        assertEquals("header", findQuestion.getHead());
        assertEquals("body", findQuestion.getBody());
        assertEquals("1", findQuestion.getAnswer());
        assertEquals(question, findQuestion);
    }

    @Test
    @DisplayName("question 을 요청하면 전부다 가져와야 한다.")
    public void givenQuestion_whenFindQuestion_thenQuestionFound() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question1 = Question.builder()
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .head("header1")
                .name("hi")
                .body("body1")
                .answer("1")
                .build();
        Question question2 = Question.builder()
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .head("header2")
                .name("hi")
                .body("body2")
                .answer("2")
                .build();
        questionService.save(question1);
        questionService.save(question2);

        //when
        List<Question> answer = questionService.findAll();

        //then
        assertEquals(2, answer.size());
        assertTrue(answer.contains(question1));
        assertTrue(answer.contains(question2));
    }
    @Test
    @DisplayName("question update 요청이 오면 업데이트 되어야 한다.")
    public void givenQuestion_whenUpdateQuestion_thenQuestionUpdated() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question = Question.builder()
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .head("header")
                .name("hi")
                .body("body")
                .answer("1")
                .build();
        questionService.save(question);

        QuestionRequest questionRequest = QuestionRequest.builder()
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .head("header2")
                .name("hi")
                .body("body2")
                .answer("2")
                .build();
        questionService.update(question.getId(), questionRequest);

        //when
        Question updatedQuestion = questionService.findById(question.getId());

        //then
        assertEquals(QuestionType.MULTIPLE_CHOICE, updatedQuestion.getQuestionType());
        assertEquals("header2", updatedQuestion.getHead());
        assertEquals("hi", updatedQuestion.getName());
        assertEquals("body2", updatedQuestion.getBody());
        assertEquals("2", updatedQuestion.getAnswer());
    }

    @Test
    @DisplayName("문제를 요청했을때 이미 횟수가 0 이면 잘못된 요청임으로 에러를 발생해야 한다.")
    void givenAlreadyZeroCount_whenGetQuestion_thenError() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question = Question.builder().questionType(QuestionType.MULTIPLE_CHOICE).head("head1").name("hi").body("body1").answer("1").build();
        questionRepository.save(question);

        ContentUserCount contentUserCount = ContentUserCount.builder().user(user).remainQuestionCount(0).remainVideoCount(1).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        Exception exception = assertThrows(NoRemainQuestionException.class, () -> questionService.question(user));

        //then
        assertEquals("오늘의 문제 풀이 횟수가 소진되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("첫 유저가 요청을 했을 때 정상적으로 잘 가져와야 한다.")
    void givenFirstUser_whenGetQuestion_thenSuccess() throws NoRemainQuestionException {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question = Question.builder().questionType(QuestionType.MULTIPLE_CHOICE).head("head1").name("hi").body("body1").answer("1").build();
        questionRepository.save(question);

        //when
        QuestionResponse response = questionService.question(user);

        //then
        assertNotNull(response);
    }

    @Test
    @DisplayName("가진 문제가 한문제만 있을땐 같은 문제를 줄수 있다.")
    void givenOneQuestion_whenGetQuestion_thenSameQuestion() throws NoRemainQuestionException {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question = Question.builder().questionType(QuestionType.MULTIPLE_CHOICE).head("head1").name("hi").body("body1").answer("1").build();
        questionRepository.save(question);

        ContentUserCount contentUserCount = ContentUserCount.builder().beforeQuestion(question.getId()).user(user).remainQuestionCount(3).remainVideoCount(1).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        QuestionResponse response = questionService.question(user);

        //then
        assertEquals(question.getId(), response.getId());
    }

    @Test
    @DisplayName("가진 문제가 두문제 이상이면 이전문제와 현재 문제가 같으면 안된다.")
    void givenTwoQuestion_whenGetQuestion_thenNotSameQuestion() throws NoRemainQuestionException {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question1 = Question.builder().questionType(QuestionType.MULTIPLE_CHOICE).head("head1").name("hi").body("body1").answer("1").build();
        questionRepository.save(question1);
        Question question2 = Question.builder().questionType(QuestionType.MULTIPLE_CHOICE).head("head2").name("hi2").body("body2").answer("2").build();
        questionRepository.save(question2);

        ContentUserCount contentUserCount = ContentUserCount.builder().beforeQuestion(question2.getId()).user(user).remainQuestionCount(3).remainVideoCount(1).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        QuestionResponse response = questionService.question(user);

        //then
        assertNotEquals(question2.getId(), response.getId());
    }

    @Test
    @DisplayName("문제를 요청했을때 이미 횟수가 0 이고, 어제 요청한거라면 오늘은 횟수 3으로 잘 가져오고 문제를 정상적으로 잘 전달 되어야 한다.")
    void givenYesterdayZeroCount_whenGetQuestion_thenSuccess() throws NoRemainQuestionException {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question1 = Question.builder().questionType(QuestionType.MULTIPLE_CHOICE).head("head1").name("hi").body("body1").answer("1").build();
        questionRepository.save(question1);

        ContentUserCount contentUserCount = ContentUserCount.builder().user(user).remainQuestionCount(0).remainVideoCount(1).resetAt(LocalDate.now().minusDays(1)).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        QuestionResponse response = questionService.question(user);

        //then
        assertNotNull(response);
    }

    @Test
    @DisplayName("문제를 한번도 받은 적 없는 유저가 post 하면 에러를 반환해야 한다.")
    void givenNoInit_whenPostQuestion_thenError() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        //when
        IllegalCallerException exception = assertThrows(IllegalCallerException.class, () -> questionService.questionPost(user, 1L, "1"));

        //then
        assertEquals("문제를 낸 적 없는 유저입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("문제의 정답을 받았을 때 소진 횟수가 이미 0 이면 에러를 반환해야 한다.")
    void givenNoRemainCount_whenPostQuestion_thenError() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question1 = Question.builder().questionType(QuestionType.MULTIPLE_CHOICE).head("head1").name("hi").body("body1").answer("1").build();
        questionRepository.save(question1);

        ContentUserCount contentUserCount = ContentUserCount.builder().beforeQuestion(question1.getId()).user(user).remainQuestionCount(0).remainVideoCount(1).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        NoRemainQuestionException exception = assertThrows(NoRemainQuestionException.class, () -> questionService.questionPost(user, 1L, "1"));

        //then
        assertEquals("오늘의 문제 풀이 횟수가 소진되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("문제를 풀었을때 못맞췄으면 incorrect 와 함께 count 가 차감되어야 한다.")
    void givenIncorrectAnswer_whenPostQuestion_thenReturnIncorrect() throws NoRemainQuestionException {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question1 = Question.builder().questionType(QuestionType.MULTIPLE_CHOICE).head("head1").name("hi").body("body1").answer("1").build();
        questionRepository.save(question1);

        ContentUserCount contentUserCount = ContentUserCount.create(user);
        contentUserCountRepository.save(contentUserCount);

        UserPoint userPoint = UserPoint.fromUser(user);
        userPointRepository.save(userPoint);

        //when
        QuestionPostDto questionPostDto = questionService.questionPost(user, 1L, "10");

        //then
        assertEquals(QuestionResultStatus.INCORRECT.name(), questionPostDto.getAnswer());
        assertEquals(2, contentUserCount.getRemainQuestionCount());
        assertEquals(0, userPoint.getUserPoint());
    }

    @Test
    @DisplayName("문제를 풀었을때 맞췄으면 correct 와 함께 포인트가 올라가야 한다.")
    void givenCorrectAnswer_whenPostQuestion_thenPointUp() throws NoRemainQuestionException {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        String answer = "1";
        Question question1 = Question.builder().questionType(QuestionType.MULTIPLE_CHOICE).head("head1").name("hi").body("body1").answer(answer).build();
        questionRepository.save(question1);

        ContentUserCount contentUserCount = ContentUserCount.create(user);
        contentUserCountRepository.save(contentUserCount);

        UserPoint userPoint = UserPoint.fromUser(user);
        userPointRepository.save(userPoint);

        //when
        QuestionPostDto questionPostDto = questionService.questionPost(user, question1.getId(), answer);

        //then
        assertEquals(QuestionResultStatus.CORRECT.name(), questionPostDto.getAnswer());
        assertEquals(2, contentUserCount.getRemainQuestionCount());
        assertEquals(10, userPoint.getUserPoint());
    }

    @Test
    @DisplayName("content remain api 를 호출할때 어제 이력이 마지막이면 reset 해야 한다.")
    void givenYesterdayUpdatedAt_whenContentRemain_thenReset() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        ContentUserCount contentUserCount = ContentUserCount.builder().resetAt(LocalDate.now().minusDays(1)).user(user).remainQuestionCount(0).remainVideoCount(0).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        ContentsRemainDto contentsRemainDto = questionService.contentsRemain(user);

        //then
        assertEquals(3, contentsRemainDto.remainQuestion);
        assertEquals(1, contentsRemainDto.remainVideo);
    }

    @Test
    @DisplayName("content remain api 를 호출할때 첫 유저이면 정상적으로 반환해야 한다.")
    void givenFirstUser_whenContentRemain_thenSuccess() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        //when
        ContentsRemainDto contentsRemainDto = questionService.contentsRemain(user);

        //then
        assertEquals(3, contentsRemainDto.remainQuestion);
        assertEquals(1, contentsRemainDto.remainVideo);
    }

    @Test
    @DisplayName("content remain api 를 호출하면 남은 content 를 반환해야 한다.")
    void givenUser_whenContentRemain_thenSuccess() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        ContentUserCount contentUserCount = ContentUserCount.builder().user(user).remainQuestionCount(0).remainVideoCount(0).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        ContentsRemainDto contentsRemainDto = questionService.contentsRemain(user);

        //then
        assertEquals(0, contentsRemainDto.remainQuestion);
        assertEquals(0, contentsRemainDto.remainVideo);
    }
}