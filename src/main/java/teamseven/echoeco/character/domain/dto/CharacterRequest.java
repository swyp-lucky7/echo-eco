package teamseven.echoeco.character.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterType;

@Data
@NoArgsConstructor
public class CharacterRequest {
    private Long id;
    @NotNull
    private CharacterType type;
    @NotNull
    private String name;
    private String descriptions;
    @NotNull
    private int maxLevel;
    private String image;
    private Boolean isPossible;
    private String completeMessages;

    public Character toEntity() {
        return Character.builder()
                .id(id)
                .type(type)
                .name(name)
                .descriptions(descriptions)
                .maxLevel(maxLevel)
                .image(image)
                .isPossible(isPossible)
                .completeMessages(completeMessages)
                .build();
    }
}
