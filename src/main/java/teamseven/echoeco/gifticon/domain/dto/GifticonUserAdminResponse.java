package teamseven.echoeco.gifticon.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.gifticon.domain.GifticonUser;

@Data
@Builder
public class GifticonUserAdminResponse {
    private Long id;
    private String email;
    private String name;
    private Boolean isSend;

    public static GifticonUserAdminResponse fromEntity(GifticonUser gifticonUser) {
        return GifticonUserAdminResponse.builder()
                .id(gifticonUser.getId())
                .email(gifticonUser.getEmail())
                .name(gifticonUser.getName())
                .isSend(gifticonUser.getIsSend())
                .build();
    }

    @QueryProjection
    public GifticonUserAdminResponse(Long id, String email, String name, Boolean isSend) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.isSend = isSend;
    }
}
