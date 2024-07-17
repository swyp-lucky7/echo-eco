package teamseven.echoeco.item.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemPickResponse {
    private ItemResponse itemResponse;
    private int userPoint;
    private int level;

    public static ItemPickResponse makeItemPickResponse(ItemResponse itemResponse, int userPoint, int level) {
        return ItemPickResponse.builder()
                .itemResponse(itemResponse)
                .userPoint(userPoint)
                .level(level)
                .build();
    }
}
