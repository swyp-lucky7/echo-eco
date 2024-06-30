package teamseven.echoeco.admin.character.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.character.domain.CharacterDetail;

@Data
public class CharacterDetailRequest {
    @NotNull
    private Long characterId;
    @NotNull
    private int level;
    @NotNull
    private String imageUrl;

    public CharacterDetail toEntity() {
        return CharacterDetail.builder()
                .character(new Character(this.characterId))
                .imageUrl(this.imageUrl)
                .level(this.level)
                .build();
    }
}
