package teamseven.echoeco.user.domain.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserTokenResponse {
    private String authorization;
}
