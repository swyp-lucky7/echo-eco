package teamseven.echoeco.admin.character.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CharacterType type;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int maxLevel;

    @Column(nullable = false)
    @Builder.Default
    private boolean isPossible = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Character(Long id) {
        this.id = id;
    }
}
