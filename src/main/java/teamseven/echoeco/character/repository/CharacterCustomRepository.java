package teamseven.echoeco.character.repository;

import teamseven.echoeco.character.domain.CharacterType;
import teamseven.echoeco.character.domain.dto.CharacterResponse;

import java.util.List;

public interface CharacterCustomRepository {
    List<CharacterResponse> searchPickList(Boolean isPossible);
}
