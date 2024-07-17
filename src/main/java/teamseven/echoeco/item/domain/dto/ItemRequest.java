package teamseven.echoeco.item.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequest {
    @NotNull
    private long itemId;
}
