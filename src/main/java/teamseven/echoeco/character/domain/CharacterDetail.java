package teamseven.echoeco.character.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CharacterDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Character character;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Environment environment;

    @Builder.Default
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();
}
