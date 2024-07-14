package teamseven.echoeco.item.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import teamseven.echoeco.item.domain.Item;

@Data
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

}
