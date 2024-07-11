package teamseven.echoeco.config.exception;

public class NotAdminSettingException extends Exception {
    public NotAdminSettingException() {
        super("어드민에서 세팅하지 않은 정보입니다.");
    }

    public NotAdminSettingException(String message) {
        super(message);
    }
}
