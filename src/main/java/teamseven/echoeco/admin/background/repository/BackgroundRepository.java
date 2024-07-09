package teamseven.echoeco.admin.background.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamseven.echoeco.admin.background.domain.Background;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {

}
