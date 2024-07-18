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
    @Builder.Default
    private String speechBubble = "안녕 난 폴라야";
    private Boolean isPossible;
    private String image;

    @QueryProjection
    public CharacterPickListDto(Long id, String name, String speechBubble, boolean isPossible, String image) {
        this.id = id;
        this.name = name;
        this.speechBubble = speechBubble;
        this.isPossible = isPossible;
        this.image = image;
    }
}
