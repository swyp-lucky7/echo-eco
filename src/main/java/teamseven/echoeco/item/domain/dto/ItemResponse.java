package teamseven.echoeco.item.domain.dto;

import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.item.domain.Item;

@Data
@Builder
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int levelUp;
    private String imageUrl;

    public static ItemResponse fromEntity(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .levelUp(item.getLevelUp())
                .imageUrl(item.getImageUrl())
                .build();
    }
}
