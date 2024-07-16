package teamseven.echoeco.question.domain.dto;

import lombok.Getter;
import teamseven.echoeco.question.domain.Question;
import teamseven.echoeco.question.domain.QuestionType;
import teamseven.echoeco.user.domain.User;

@Getter
public class QuestionResponse {

    private final String name;
    private final QuestionType questionType;
    private final String head;
    private final String body;
    private final String answer;
//    private final User makeUser;


    public QuestionResponse(Question question) {
        this.name = question.getName();
        this.questionType = question.getQuestionType();
        this.head = question.getHead();
        this.body = question.getBody();
        this.answer = question.getAnswer();
//        this.makeUser = question.getMakeUser();
    }
}
