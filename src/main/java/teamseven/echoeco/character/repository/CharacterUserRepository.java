package teamseven.echoeco.character.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.user.domain.User;

public interface CharacterUserRepository extends JpaRepository<CharacterUser, Long> {
    @Query("SELECT " +
            "CASE WHEN cu IS NULL THEN FALSE ELSE TRUE END" +
            " FROM CharacterUser cu WHERE cu.user = :user AND cu.isUse = true")
    boolean IsUse(User user);

    @Query("SELECT cu FROM CharacterUser cu WHERE cu.user = :user AND cu.isUse = true")
    CharacterUser findByUserWithUse(User user);

    CharacterUser findByUser(User user);
}
