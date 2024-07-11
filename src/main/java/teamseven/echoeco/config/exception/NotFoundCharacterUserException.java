package teamseven.echoeco.config.exception;

public class NotFoundCharacterUserException extends Exception {
    public NotFoundCharacterUserException() {
        super("해당 유저가 사용하고 있는 캐릭터가 없습니다.");
    }
}
