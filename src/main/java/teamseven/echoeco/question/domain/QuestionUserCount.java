package teamseven.echoeco.question.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.user.domain.User;

import java.time.LocalDate;

import static teamseven.echoeco.config.Constants.DEFAULT_IMAGE_URL;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionUserCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_user_count_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long beforeQuestion;

    @Builder.Default
    private int remainCount = 0;

    @Builder.Default
    private LocalDate updatedAt = LocalDate.now();

    public static QuestionUserCount reset() {
        return QuestionUserCount.builder()
                .beforeQuestion(null)
                .remainCount(3)
                .updatedAt(LocalDate.now())
                .build();
    }

    public void update(Long id) {
        this.beforeQuestion = id;
        this.remainCount -= 1;
        this.updatedAt = LocalDate.now();
    }
}
