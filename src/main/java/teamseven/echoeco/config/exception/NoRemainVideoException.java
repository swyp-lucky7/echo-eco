package teamseven.echoeco.config.exception;

public class NoRemainVideoException extends Exception {
    public NoRemainVideoException() {
        super("오늘의 영상 횟수가 소진되었습니다.");
    }
}
