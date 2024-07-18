package teamseven.echoeco.character.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterTrashDto {
    private String backgroundImage;
    private String characterImage;
}
