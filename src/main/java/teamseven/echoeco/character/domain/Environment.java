package teamseven.echoeco.character.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Environment {
    TRASH("trash", "쓰레기"),
    CLEAN("clean", "깨끗");

    private final String name;
    private final String koreanName;
}
