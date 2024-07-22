package teamseven.echoeco.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "userJwts")
public class UserJWT {
    @Id
    @GeneratedValue
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Builder
    public UserJWT(User user) {
        this.user = user;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}
