package teamseven.echoeco.admin.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.admin.character.domain.CharacterDetail;

import java.util.List;

@Repository
public interface CharacterDetailRepository extends JpaRepository<CharacterDetail, Long> {
    List<CharacterDetail> findByCharacter_IdOrderByLevelAsc(Long characterId);
}
