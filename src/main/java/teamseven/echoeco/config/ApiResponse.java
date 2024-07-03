package teamseven.echoeco.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String responseMessage;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return res(200, "ok", data);
    }

    public static <T> ApiResponse<T> res(int statusCode, String responseMessage, T data) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .data(data)
                .build();
    }
}
