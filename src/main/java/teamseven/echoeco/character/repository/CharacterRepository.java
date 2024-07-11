package teamseven.echoeco.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.character.domain.Character;

public interface CharacterRepository extends JpaRepository<Character, Long>, CharacterCustomRepository {
}
