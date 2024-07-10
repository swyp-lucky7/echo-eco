package teamseven.echoeco.character.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CharacterPickRequest {
    @NotNull
    private Long characterId;
}
