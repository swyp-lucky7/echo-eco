package teamseven.echoeco.question.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamseven.echoeco.user.domain.User;

import java.time.LocalDate;

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
    private int remainQuestionCount = 0;

    @Builder.Default
    private int remainVideoCount = 0;

    @Builder.Default
    private LocalDate updatedAt = LocalDate.now();

    public static QuestionUserCount create() {
        return QuestionUserCount.builder()
                .beforeQuestion(null)
                .remainQuestionCount(3)
                .remainVideoCount(1)
                .build();
    }

    public void reset() {
        this.remainQuestionCount = 3;
        this.remainVideoCount = 1;
        this.updatedAt = LocalDate.now();
    }

    public void update(Long id) {
        this.beforeQuestion = id;
        this.updatedAt = LocalDate.now();
    }

    public void downRemainQuestionCount() {
        this.remainQuestionCount--;
    }

    public void fillRemainQuestionCount() {
        this.remainQuestionCount = 3;
        this.remainVideoCount = 0;
    }
}
