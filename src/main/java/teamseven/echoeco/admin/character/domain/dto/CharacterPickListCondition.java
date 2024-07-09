package teamseven.echoeco.admin.character.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.admin.character.domain.CharacterType;

@Data
@Builder
public class CharacterPickListCondition {
    private CharacterType type;
    private Boolean isPossible;
}
