package teamseven.echoeco.question.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import teamseven.echoeco.user.domain.User;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentUserCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_user_count_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long beforeQuestion;

    @Builder.Default
    private int remainQuestionCount = 0;

    @Builder.Default
    private int remainVideoCount = 0;

    @LastModifiedDate
    @Builder.Default
    private LocalDate updatedAt = LocalDate.now();

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
        this.updatedAt = LocalDate.now();
    }

    public void update(Long id) {
        this.beforeQuestion = id;
        this.updatedAt = LocalDate.now();
    }

    public void subtractRemainQuestionCount() {
        this.remainQuestionCount--;
    }

    public void watchedVideo() {
        this.remainQuestionCount = 3;
        this.remainVideoCount = 0;
    }
}
