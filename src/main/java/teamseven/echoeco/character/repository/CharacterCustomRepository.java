package teamseven.echoeco.character.repository;

import teamseven.echoeco.character.domain.dto.CharacterPickListDto;

import java.util.List;

public interface CharacterCustomRepository {
    List<CharacterPickListDto> searchPickList(Boolean isPossible);
}
