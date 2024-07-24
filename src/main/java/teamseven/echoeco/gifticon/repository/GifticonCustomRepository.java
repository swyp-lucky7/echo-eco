package teamseven.echoeco.gifticon.repository;

import teamseven.echoeco.gifticon.domain.dto.GifticonAdminResponse;

import java.util.List;

public interface GifticonCustomRepository {
    List<GifticonAdminResponse> search(String email, Boolean isSend);
}
