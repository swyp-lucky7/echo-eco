package teamseven.echoeco.trash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.trash.domain.TrashUser;
import teamseven.echoeco.user.domain.User;

import java.util.Optional;

public interface TrashUserRepository extends JpaRepository<TrashUser, Long> {
    Optional<TrashUser> findByUser(User user);
}
