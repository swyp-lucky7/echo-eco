package teamseven.echoeco.admin.question.domain.dto;

import lombok.Getter;
import teamseven.echoeco.admin.question.domain.Question;
import teamseven.echoeco.admin.question.domain.QuestionType;
import teamseven.echoeco.user.domain.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class QuestionResponse {

//    private final String name;
//    private final QuestionType questionType;
    private final String head;
    private final String body;
    private final String answer;
//    private final User makeUser;
//    private final LocalDateTime createdAt;
//    private final LocalDateTime updatedAt;


    public QuestionResponse(Question question) {
//        this.name = question.getName();
//        this.questionType = question.getQuestionType();
        this.head = question.getHead();
        this.body = question.getBody();
        this.answer = question.getAnswer();
//        this.makeUser = question.getMakeUser();
//        this.createdAt = question.getCreatedAt();
//        this.updatedAt = question.getUpdatedAt();
    }
}
