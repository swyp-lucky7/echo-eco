package teamseven.echoeco.admin.character.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.admin.character.domain.Character;
import teamseven.echoeco.admin.character.domain.CharacterDetail;
import teamseven.echoeco.admin.character.domain.Environment;

@Data
@Builder
public class CharacterDetailDto {
    private Long id;
    @NotNull
    private Long characterId;
    @NotNull
    private int level;
    @NotNull
    private String imageUrl;
    private Environment environment;

    public CharacterDetail toEntity() {
        return CharacterDetail.builder()
                .id(id)
                .character(new Character(this.characterId))
                .imageUrl(this.imageUrl)
                .level(this.level)
                .environment(this.environment)
                .build();
    }

    public static CharacterDetailDto fromDto(CharacterDetail characterDetail) {
        return CharacterDetailDto.builder()
                .id(characterDetail.getId())
                .level(characterDetail.getLevel())
                .imageUrl(characterDetail.getImageUrl())
                .environment(characterDetail.getEnvironment())
                .build();
    }
}
