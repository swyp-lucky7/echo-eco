package teamseven.echoeco.trash.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrashStatusDto {
    private Boolean isClean;
}
