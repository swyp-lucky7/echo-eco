package teamseven.echoeco.character.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.character.domain.Character;

@Data
@Builder
@AllArgsConstructor
public class CharacterResponse {
    private Long id;
    private String name;
    private String descriptions;
    private boolean isPossible;

    public static CharacterResponse fromEntity(Character character) {
        return CharacterResponse.builder()
                .id(character.getId())
                .name(character.getName())
                .descriptions(character.getDescriptions())
                .isPossible(character.isPossible())
                .build();
    }
}
