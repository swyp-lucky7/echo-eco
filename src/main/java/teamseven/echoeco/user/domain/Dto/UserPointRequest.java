package teamseven.echoeco.user.domain.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPointRequest {
    @NotNull
    private int userPoint;
}
