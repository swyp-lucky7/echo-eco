package teamseven.echoeco.character.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterType;

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
    private String pickImage;
    private String frameImage;
    private Boolean isPossible;
    private String completeMessages;

    public Character toEntity() {
        return Character.builder()
                .id(id)
                .type(type)
                .name(name)
                .descriptions(descriptions)
                .maxLevel(maxLevel)
                .pickImage(pickImage)
                .frameImage(frameImage)
                .isPossible(isPossible)
                .completeMessages(completeMessages)
                .build();
    }
}
