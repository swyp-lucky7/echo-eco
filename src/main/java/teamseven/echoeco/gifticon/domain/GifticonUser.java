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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_user_id")
    private CharacterUser characterUser;

    @Builder.Default
    private String name = "백다방 아메리카노";

    private String email;
    
    private String imageUrl;

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

    public void sendGift(String imageUrl, User sendUser) {
        this.imageUrl = imageUrl;
        this.sendAdminName = sendUser.getName();
        this.isSend = true;
        this.sentAt = LocalDateTime.now();
    }
}
