package teamseven.echoeco.gifticon.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GifticonUserAdminSendRequest {
    @NotNull
    private String userEmail;

    @NotNull
    private String number;
}
