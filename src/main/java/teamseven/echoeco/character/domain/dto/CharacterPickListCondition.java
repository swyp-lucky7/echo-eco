package teamseven.echoeco.character.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.character.domain.CharacterType;

@Data
@Builder
public class CharacterPickListCondition {
    private CharacterType type;
    private Boolean isPossible;
}
