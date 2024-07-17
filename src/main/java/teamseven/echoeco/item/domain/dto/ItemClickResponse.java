package teamseven.echoeco.item.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemClickResponse {
    private ItemResponse itemResponse;
    private int userPoint;
    private boolean availableBuy;

    public static ItemClickResponse makeItemClickResponse(ItemResponse itemResponse, int userPoint,
                                                          boolean availableBuy) {
        return ItemClickResponse.builder()
                .itemResponse(itemResponse)
                .userPoint(userPoint)
                .availableBuy(availableBuy)
                .build();
    }


}
