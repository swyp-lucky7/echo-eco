package teamseven.echoeco.background.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import teamseven.echoeco.character.domain.Environment;

import java.time.LocalDateTime;

import static teamseven.echoeco.config.Constants.DEFAULT_IMAGE_URL;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Background {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "background_id")
    private Long id;

    private String name;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Environment environment;

    @Column(nullable = false)
    private int level;

    @Builder.Default
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static Background empty() {
        return Background.builder()
                .name("")
                .image(DEFAULT_IMAGE_URL)
                .environment(Environment.CLEAN)
                .level(0)
                .build();
    }
}
