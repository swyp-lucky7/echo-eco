package teamseven.echoeco.trash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.trash.domain.TrashUser;

import java.util.Optional;

public interface TrashUserRepository extends JpaRepository<TrashUser, Long> {
    Optional<TrashUser> findByUser_Id(Long id);
}
