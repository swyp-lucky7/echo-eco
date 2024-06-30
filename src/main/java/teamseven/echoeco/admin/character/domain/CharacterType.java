package teamseven.echoeco.admin.character.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CharacterType {
    ANIMAL("animal", "동물"),
    PLANT("plant", "식물");

    private final String name;
    private final String koreanName;
}
