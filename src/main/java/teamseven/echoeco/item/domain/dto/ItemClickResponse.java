package teamseven.echoeco.item.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemClickResponse {
    private ItemResponse itemResponse;
    private int user_point;
    private boolean available_buy;

    public static ItemClickResponse makeItemClickResponse(ItemResponse itemResponse, int user_point,
                                                          boolean available_buy) {
        return ItemClickResponse.builder()
                .itemResponse(itemResponse)
                .user_point(user_point)
                .available_buy(available_buy)
                .build();
    }


}
