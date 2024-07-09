package teamseven.echoeco.admin.character.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.character.domain.CharacterType;

@Data
@Builder
public class CharacterResponse {
    private Long id;
    private CharacterType type;
    private String name;
    private String descriptions;
    private int maxLevel;
    private boolean isPossible;

    public static CharacterResponse fromEntity(Character character) {
        return CharacterResponse.builder()
                .type(character.getType())
                .name(character.getName())
                .descriptions(character.getDescriptions())
                .maxLevel(character.getMaxLevel())
                .isPossible(character.isPossible())
                .build();
    }

    @QueryProjection
    public CharacterResponse(Long id, CharacterType type, String name, String descriptions, int maxLevel, boolean isPossible) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.descriptions = descriptions;
        this.maxLevel = maxLevel;
        this.isPossible = isPossible;
    }
}
