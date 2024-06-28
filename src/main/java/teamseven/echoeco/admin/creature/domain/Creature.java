package teamseven.echoeco.admin.creature.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Creature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creature_id")
    private Long creature;

    @Column(nullable = false)
    private CreatureType type;
    @Column(nullable = false)
    private String name;

    private String explain;

    @Column(nullable = false)
    private int level;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
