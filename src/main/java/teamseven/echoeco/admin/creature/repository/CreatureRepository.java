package teamseven.echoeco.admin.creature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.admin.creature.domain.Creature;

@Repository
public interface CreatureRepository extends JpaRepository<Creature, Long> {
}
