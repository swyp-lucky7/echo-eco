package teamseven.echoeco.gifticon.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GifticonUserAdminRequest {
    private String userEmail;
    private Boolean isSend;
}
