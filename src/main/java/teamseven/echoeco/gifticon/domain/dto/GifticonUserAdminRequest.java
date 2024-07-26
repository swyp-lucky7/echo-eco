package teamseven.echoeco.gifticon.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GifticonUserAdminRequest {
    private String userEmail;
    private Boolean isSend;
}
