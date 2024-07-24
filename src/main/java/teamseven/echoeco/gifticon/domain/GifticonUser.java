package teamseven.echoeco.gifticon.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class GifticonUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gifticon_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "character_user_id")
    private CharacterUser characterUser;

    @Builder.Default
    private String name = "백다방 아메리카노";
    
    private String number;

    @Builder.Default
    private Boolean isSend = false;

    private String sendAdminName;

    private LocalDateTime sentAt;

    @CreatedDate
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void sendGift(String number, User sendUser) {
        this.number = number;
        this.sendAdminName = sendUser.getName();
        this.isSend = true;
        this.sentAt = LocalDateTime.now();
    }
}
