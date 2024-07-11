package teamseven.echoeco.background.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.background.domain.TrashUser;

import java.util.Optional;

public interface TrashUserRepository extends JpaRepository<TrashUser, Long> {
    Optional<TrashUser> findByUser_Id(Long id);
}
