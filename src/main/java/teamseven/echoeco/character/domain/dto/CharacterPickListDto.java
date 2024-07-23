package teamseven.echoeco.character.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CharacterPickListDto {
    private Long id;
    private String name;
    private String descriptions;
    private Boolean isPossible;
    private String image;

    @QueryProjection
    public CharacterPickListDto(Long id, String name, String descriptions, boolean isPossible, String image) {
        this.id = id;
        this.name = name;
        this.descriptions = descriptions;
        this.isPossible = isPossible;
        this.image = image;
    }
}
