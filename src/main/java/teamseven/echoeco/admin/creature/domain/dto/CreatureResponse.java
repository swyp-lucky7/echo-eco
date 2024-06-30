package teamseven.echoeco.admin.creature.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.admin.creature.domain.Creature;
import teamseven.echoeco.admin.creature.domain.CreatureType;

@Data
@Builder
public class CreatureResponse {
    private CreatureType type;
    private String name;
    private String description;
    private int maxLevel;

    public static CreatureResponse fromEntity(Creature creature) {
        return CreatureResponse.builder()
                .type(creature.getType())
                .name(creature.getName())
                .description(creature.getDescription())
                .maxLevel(creature.getMaxLevel())
                .build();
    }
}
