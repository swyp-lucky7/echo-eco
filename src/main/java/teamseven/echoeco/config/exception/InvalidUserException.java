package teamseven.echoeco.config.exception;

import jakarta.servlet.ServletException;

public class InvalidUserException extends ServletException {

    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException() {super("로그인 정보가 정확하지 않습니다.");}
}
