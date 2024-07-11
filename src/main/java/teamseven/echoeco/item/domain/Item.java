package teamseven.echoeco.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamseven.echoeco.item.domain.dto.ItemDto;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private String imageUrl;

    private Boolean isUse;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    public static Item empty() {
        return Item.builder()
                .name("")
                .imageUrl("/images/yes.png")
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
