package teamseven.echoeco.character.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.character.domain.CharacterDetail;
import teamseven.echoeco.character.domain.Environment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterDetailRepository extends JpaRepository<CharacterDetail, Long> {
    List<CharacterDetail> findByCharacter_IdOrderByLevelAsc(Long characterId);

    @Query("SELECT cd FROM CharacterDetail cd WHERE cd.level <= :level AND cd.environment = :environment ORDER BY cd.level DESC")
    List<CharacterDetail> findByLevelAndEnvironment(int level, Environment environment, Pageable pageable);
}
