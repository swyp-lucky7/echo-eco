package teamseven.echoeco.character.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.background.domain.Background;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.user.domain.UserPoint;

@Data
@Builder
public class CharacterUserResponse {
    private Character character;
    private int level;
    private Environment environment;
    private String backgroundImage;
    private String characterImage;
    private Integer userPoint;

    public static CharacterUserResponse fromEntity(CharacterUser characterUser,
                                                   Environment environment,
                                                   String backgroundImage,
                                                   String characterImage,
                                                   UserPoint userPoint) {
        return CharacterUserResponse.builder()
                .character(characterUser.getCharacter())
                .level(characterUser.getLevel())
                .environment(environment)
                .backgroundImage(backgroundImage)
                .characterImage(characterImage)
                .userPoint(userPoint.getUserPoint())
                .build();

    }
}
