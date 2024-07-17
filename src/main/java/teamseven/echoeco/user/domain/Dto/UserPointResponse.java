package teamseven.echoeco.user.domain.Dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.user.domain.UserPoint;

@Data
@Builder
public class UserPointResponse {
    private int userPoint;

    public static UserPointResponse fromEntity(UserPoint userPoint) {
        return UserPointResponse.builder()
                .userPoint(userPoint.getUserPoint())
                .build();
    }
}
