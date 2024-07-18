package teamseven.echoeco.character.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterType;

@Data
@Builder
@AllArgsConstructor
public class CharacterResponse {
    private Long id;
    private String name;
    private String descriptions;
    private int maxLevel;
    private boolean isPossible;

    public static CharacterResponse fromEntity(Character character) {
        return CharacterResponse.builder()
                .id(character.getId())
                .name(character.getName())
                .descriptions(character.getDescriptions())
                .maxLevel(character.getMaxLevel())
                .isPossible(character.isPossible())
                .build();
    }

    @QueryProjection
    public CharacterResponse(Long id, CharacterType type, String name, String descriptions, int maxLevel, boolean isPossible) {
        this.id = id;
        this.name = name;
        this.descriptions = descriptions;
        this.maxLevel = maxLevel;
        this.isPossible = isPossible;
    }
}
