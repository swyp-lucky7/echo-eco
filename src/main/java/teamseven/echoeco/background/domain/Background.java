package teamseven.echoeco.background.domain;

import static teamseven.echoeco.config.Constants.DEFAULT_IMAGE_URL;

import jakarta.persistence.*;
import lombok.*;
import teamseven.echoeco.character.domain.Environment;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Background {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "background_id")
    private Long id;

    private String name;

    @Column(nullable = false)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Environment environment;

    @Column(nullable = false)
    private int level;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
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
