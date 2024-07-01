package teamseven.echoeco.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
