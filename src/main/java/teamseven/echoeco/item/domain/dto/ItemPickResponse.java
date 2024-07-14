package teamseven.echoeco.item.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemPickResponse {
    private ItemResponse itemResponse;
    private int user_point;
    private int level;

    public static ItemPickResponse makeItemPickResponse(ItemResponse itemResponse, int user_point, int level) {
        return ItemPickResponse.builder()
                .itemResponse(itemResponse)
                .user_point(user_point)
                .level(level)
                .build();
    }
}
