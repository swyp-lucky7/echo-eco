package teamseven.echoeco.gifticon.repository;

import teamseven.echoeco.gifticon.domain.dto.GifticonUserAdminResponse;

import java.util.List;

public interface GifticonCustomRepository {
    List<GifticonUserAdminResponse> search(String email, Boolean isSend);
}
