package teamseven.echoeco.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import teamseven.echoeco.config.ApiResponse;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<String> illegalExHandler(IllegalArgumentException e) {
        return ApiResponse.res(HttpStatus.BAD_REQUEST.value(), e.getMessage(), "error");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiResponse<String> internalServerExHandler(Exception e) {
        return ApiResponse.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), "error");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> NotAdminSettingHandler(NotAdminSettingException e) {
        return ApiResponse.res(HttpStatus.BAD_REQUEST.value(), e.getMessage(), "Not Admin Setting");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalCallerException.class)
    public ApiResponse<String> illegalCallerHandler(IllegalCallerException e) {
        return ApiResponse.res(HttpStatus.BAD_REQUEST.value(), e.getMessage(), "존재 할 수 없는 요청입니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NoRemainVideoException.class, NoRemainQuestionException.class})
    public ApiResponse<String> noRemainException(Exception e) {
        return ApiResponse.success(e.getMessage());
    }

    // 유저의 캐릭터가 없는 경우 400 에러로 리턴
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> NoCharacterExHandler(NotFoundCharacterUserException e) {
        return ApiResponse.res(HttpStatus.BAD_REQUEST.value(), e.getMessage(), "No Character");
    }
}
