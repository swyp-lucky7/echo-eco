package teamseven.echoeco.background.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.background.domain.Background;
import teamseven.echoeco.character.domain.Environment;

import java.util.List;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {

    @Query("SELECT b FROM Background b WHERE b.level <= :level AND b.environment = :environment ORDER BY b.level DESC")
    List<Background> findByLevelAndEnvironment(int level, Environment environment, Pageable pageable);
}
