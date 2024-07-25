package teamseven.echoeco.gifticon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.gifticon.domain.GifticonUser;
import teamseven.echoeco.user.domain.User;

import java.util.Optional;

public interface GifticonRepository extends JpaRepository<GifticonUser, Long>, GifticonCustomRepository {
    Optional<GifticonUser> findByUserAndIsSend(User user, Boolean isSend);
}
