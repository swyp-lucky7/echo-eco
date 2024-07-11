package teamseven.echoeco.item.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.item.domain.Item;

@Data
@Builder
public class ItemDto {
    @NotNull
    private String name;

    @NotNull
    private String imageUrl;

    private String description;

    @NotNull
    private int price;

    @NotNull
    private int levelUp;

    @NotNull
    private Boolean isUse;

    public Item toEntity() {
        return Item.builder()
                .name(name)
                .imageUrl(imageUrl)
                .description(description)
                .price(price)
                .levelUp(levelUp)
                .isUse(isUse)
                .build();
    }

    public static ItemDto fromEntity(Item item) {
        System.out.println(item.getImageUrl());
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .isUse(item.getIsUse())
                .levelUp(item.getLevelUp())
                .price(item.getPrice())
                .imageUrl(item.getImageUrl())
                .build();
    }
}
