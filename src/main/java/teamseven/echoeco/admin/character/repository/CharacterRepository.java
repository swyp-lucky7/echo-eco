package teamseven.echoeco.admin.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.admin.character.domain.Character;

public interface CharacterRepository extends JpaRepository<Character, Long>, CharacterCustomRepository {
}
