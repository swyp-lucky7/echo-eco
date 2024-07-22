package teamseven.echoeco.question.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import teamseven.echoeco.question.domain.dto.QuestionRequest;
import teamseven.echoeco.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String head;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User makeUser;

    @Builder.Default
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void update(QuestionRequest questionRequest) {
        this.name = questionRequest.getName();
        this.questionType = questionRequest.getQuestionType();
        this.head = questionRequest.getHead();
        this.body = questionRequest.getBody();
        this.answer = questionRequest.getAnswer();
        this.updatedAt = LocalDateTime.now();
    }

    public QuestionResultStatus isCorrect(String answer) {
        if (this.answer.equals(answer)) {
            return QuestionResultStatus.CORRECT;
        }
        return QuestionResultStatus.INCORRECT;
    }
}
