package teamseven.echoeco.admin.creature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.admin.creature.domain.CreatureDetail;

@Repository
public interface CreatureDetailRepository extends JpaRepository<CreatureDetail, Long> {
}
