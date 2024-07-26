package teamseven.echoeco.character.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterDetail;
import teamseven.echoeco.character.domain.Environment;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
