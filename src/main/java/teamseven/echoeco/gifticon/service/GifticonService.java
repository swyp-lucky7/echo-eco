package teamseven.echoeco.gifticon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.gifticon.domain.GifticonUser;
import teamseven.echoeco.gifticon.domain.dto.GifticonUserAdminResponse;
import teamseven.echoeco.gifticon.domain.dto.GifticonUserAdminSendRequest;
import teamseven.echoeco.gifticon.repository.GifticonRepository;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GifticonService {
    private final GifticonRepository gifticonRepository;
    private final UserService userService;

    public List<GifticonUserAdminResponse> search(String userEmail, Boolean isSend) {
        return gifticonRepository.search(userEmail, isSend);
    }

    public void send(GifticonUserAdminSendRequest request, User admin) {
        String userEmail = request.getUserEmail();
        User user = userService.findUserByEmail(userEmail);

        Optional<GifticonUser> gifticonUserOptional = gifticonRepository.findByUserAndIsSend(user, true);
        if (gifticonUserOptional.isEmpty()) {
            throw new IllegalArgumentException("요청하신 유저는 기프티콘 받을 조건이 되지 않았습니다.");
        }

        GifticonUser gifticonUser = gifticonUserOptional.get();
        gifticonUser.sendGift(request.getNumber(), admin);
        gifticonRepository.save(gifticonUser);
    }
}
