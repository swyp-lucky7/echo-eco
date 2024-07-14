package teamseven.echoeco.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;

public interface UserPointRepository extends JpaRepository<UserPoint, Long> {
    UserPoint findByUser(User user);
}
