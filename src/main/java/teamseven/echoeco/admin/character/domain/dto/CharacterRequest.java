package teamseven.echoeco.admin.character.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.character.domain.CharacterType;

@Data
public class CharacterRequest {
    private Long id;
    @NotNull
    private CharacterType type;
    @NotNull
    private String name;
    private String descriptions;
    @NotNull
    private int maxLevel;
    private boolean isPossible;

    public Character toEntity() {
        return Character.builder()
                .id(id)
                .type(type)
                .name(name)
                .descriptions(descriptions)
                .maxLevel(maxLevel)
                .isPossible(isPossible)
                .build();
    }
}
