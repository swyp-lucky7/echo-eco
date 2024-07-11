package teamseven.echoeco.background.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.background.domain.Background;
import teamseven.echoeco.character.domain.Environment;

@Data
@Builder
public class BackgroundDto {
    private Long id;
    private String name;

    @NotNull
    private String image;
    @NotNull
    private Environment environment;
    @NotNull
    private int level;

    public Background toEntity() {
        return Background.builder()
                .id(this.id)
                .name(this.name)
                .image(this.image)
                .environment(this.environment)
                .level(this.level)
                .build();
    }
}
