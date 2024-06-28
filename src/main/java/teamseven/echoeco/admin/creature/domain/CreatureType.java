package teamseven.echoeco.admin.creature.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CreatureType {
    ANIMAL("animal", "동물"),
    PLANT("plant", "식물");

    private final String name;
    private final String koreanName;
}
