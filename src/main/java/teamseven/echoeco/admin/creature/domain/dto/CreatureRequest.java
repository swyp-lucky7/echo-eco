package teamseven.echoeco.admin.creature.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import teamseven.echoeco.admin.creature.domain.Creature;
import teamseven.echoeco.admin.creature.domain.CreatureType;

@Data
public class CreatureRequest {
    private Long id;
    @NotNull
    private CreatureType type;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private int maxLevel;

    public Creature toEntity() {
        return Creature.builder()
                .id(id)
                .type(type)
                .name(name)
                .description(description)
                .maxLevel(maxLevel)
                .build();
    }
}
