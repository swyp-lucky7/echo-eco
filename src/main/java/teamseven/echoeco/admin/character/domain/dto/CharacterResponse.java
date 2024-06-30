package teamseven.echoeco.admin.character.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.character.domain.CharacterType;

@Data
@Builder
public class CharacterResponse {
    private CharacterType type;
    private String name;
    private String description;
    private int maxLevel;

    public static CharacterResponse fromEntity(Character character) {
        return CharacterResponse.builder()
                .type(character.getType())
                .name(character.getName())
                .description(character.getDescription())
                .maxLevel(character.getMaxLevel())
                .build();
    }
}
