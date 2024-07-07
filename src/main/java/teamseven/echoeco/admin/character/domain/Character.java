package teamseven.echoeco.admin.character.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Characters")
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

    private String descriptions;

    @Column(nullable = false)
    private int maxLevel;

    @Column(nullable = false)
    @Builder.Default
    private boolean isPossible = true;

    @Column(nullable = false)
    private String pickImage;

    @Column(nullable = false)
    private String frameImage;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Character(Long id) {
        this.id = id;
    }

    public static Character empty() {
        return Character.builder()
                .name("")
                .pickImage("/images/vendor/icons/unicons/chart.png")
                .frameImage("/images/vendor/icons/unicons/chart.png")
                .descriptions("")
                .maxLevel(100)
                .isPossible(true)
                .build();
    }
}
