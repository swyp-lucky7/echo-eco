package teamseven.echoeco.admin.creature.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import teamseven.echoeco.admin.creature.domain.Creature;
import teamseven.echoeco.admin.creature.domain.CreatureDetail;

@Data
public class CreatureDetailRequest {
    @NotNull
    private Long creatureId;
    @NotNull
    private int level;
    @NotNull
    private String imageUrl;

    public CreatureDetail toEntity() {
        return CreatureDetail.builder()
                .creature(new Creature(this.creatureId))
                .imageUrl(this.imageUrl)
                .level(this.level)
                .build();
    }
}
