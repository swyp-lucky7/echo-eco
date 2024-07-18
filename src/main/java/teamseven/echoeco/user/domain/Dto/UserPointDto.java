package teamseven.echoeco.user.domain.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserPointDto {
    private int addPoint;
    private int afterPoint;
}
