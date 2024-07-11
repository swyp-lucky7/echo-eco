package teamseven.echoeco.config.exception;

public class AlreadyCleanTrashException extends Exception{
    public AlreadyCleanTrashException() {
        super("이미 오늘 쓰레기 청소를 한 유저입니다.");
    }
}
