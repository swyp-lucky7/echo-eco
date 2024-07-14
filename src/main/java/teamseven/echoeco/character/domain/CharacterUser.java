package teamseven.echoeco.character.domain;

import jakarta.persistence.*;
import lombok.*;
import teamseven.echoeco.user.domain.User;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int level;

    private String name;

    private Boolean isUse;

    // 아이템 구매에 따른 캐릭터 레벨 증가를 위해 추가함.
    public CharacterUser updateLevel(int level) {
        this.level += level;
        return this;
    }
}
