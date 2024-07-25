package teamseven.echoeco.item.domain;

import static teamseven.echoeco.config.Constants.DEFAULT_IMAGE_URL;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import teamseven.echoeco.item.domain.dto.ItemDto;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int levelUp;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String imageUrl;

    private Boolean isUse;

    @Builder.Default
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static Item empty() {
        return Item.builder()
                .name("")
                .imageUrl(DEFAULT_IMAGE_URL)
                .description("")
                .price(1)
                .levelUp(1)
                .isUse(true)
                .build();
    }

    public void updateByItemDto(ItemDto itemDto) {
        this.name = itemDto.getName();
        this.description = itemDto.getDescription();
        this.price = itemDto.getPrice();
        this.levelUp = itemDto.getLevelUp();
        this.imageUrl = itemDto.getImageUrl();
        this.isUse = itemDto.getIsUse();
        this.updatedAt = LocalDateTime.now();
    }
}
