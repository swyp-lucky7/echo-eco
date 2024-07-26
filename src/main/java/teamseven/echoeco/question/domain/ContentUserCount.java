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
public class ContentUserCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_user_count_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long beforeQuestion;

    @Builder.Default
    private int remainQuestionCount = 0;

    @Builder.Default
    private int remainVideoCount = 0;

    @Builder.Default
    private LocalDate resetAt = LocalDate.now();

    public static ContentUserCount create(User user) {
        return ContentUserCount.builder()
                .user(user)
                .beforeQuestion(-1L)
                .remainQuestionCount(3)
                .remainVideoCount(1)
                .build();
    }

    public void reset() {
        this.remainQuestionCount = 3;
        this.remainVideoCount = 1;
        this.resetAt = LocalDate.now();
    }

    public void update(Long id) {
        this.beforeQuestion = id;
    }

    public void subtractRemainQuestionCount() {
        this.remainQuestionCount--;
    }

    public void watchedVideo() {
        this.remainQuestionCount = 3;
        this.remainVideoCount = 0;
    }
}
