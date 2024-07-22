package teamseven.echoeco.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserJWT;

public interface UserJWTRepository extends JpaRepository<UserJWT, Long> {
    Optional<UserJWT> findByUser(User user);
}
