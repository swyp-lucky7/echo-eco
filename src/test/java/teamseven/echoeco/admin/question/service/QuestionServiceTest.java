package teamseven.echoeco.admin.question.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.admin.question.domain.Question;
import teamseven.echoeco.admin.question.domain.QuestionType;
import teamseven.echoeco.admin.question.repository.QuestionRepository;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class QuestionServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;

    QuestionService questionService;

    @BeforeEach
    void setUp() {
        questionService = new QuestionService(questionRepository);
    }

    @Test
    @DisplayName("question save 요청이 오면 저장이 되어야 한다.")
    public void givenQuestion_whenSaveQuestion_thenQuestionSaved() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Question question = Question.builder()
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .head("header")
                .body("body")
                .answer("1")
                .makeUser(user)
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
                .body("body1")
                .answer("1")
                .makeUser(user)
                .build();
        Question question2 = Question.builder()
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .head("header2")
                .body("body2")
                .answer("2")
                .makeUser(user)
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
    @DisplayName("자신이 만든 question 을 요청하면 자신이 만든 question 만 응답해야 한다")
    public void givenQuestion_whenFindQuestionWithUser_thenReturnQuestion() {
        User user1 = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user1);
        User user2 = new User("name2", "email2@aaa.com", "bb", Role.USER);
        userRepository.save(user2);

        Question question1 = Question.builder()
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .head("header1")
                .body("body1")
                .answer("1")
                .makeUser(user1)
                .build();
        Question question2 = Question.builder()
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .head("header2")
                .body("body2")
                .answer("2")
                .makeUser(user2)
                .build();
        questionService.save(question1);
        questionService.save(question2);

        //when
        List<Question> byUser = questionService.findByUser(user2);

        //then
        assertEquals(1, byUser.size());
        assertTrue(byUser.contains(question2));
    }

}