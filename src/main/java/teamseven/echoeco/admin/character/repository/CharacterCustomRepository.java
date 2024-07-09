package teamseven.echoeco.admin.character.repository;

import teamseven.echoeco.admin.character.domain.CharacterType;
import teamseven.echoeco.admin.character.domain.dto.CharacterPickListCondition;
import teamseven.echoeco.admin.character.domain.dto.CharacterResponse;

import java.util.List;

public interface CharacterCustomRepository {
    public List<CharacterResponse> searchPickList(CharacterType type, Boolean isPossible);
}
