package teamseven.echoeco.config.exception;

public class NoRemainQuestionException extends Exception {
    public NoRemainQuestionException() { super("오늘의 문제 풀이 횟수가 소진되었습니다."); }
}
