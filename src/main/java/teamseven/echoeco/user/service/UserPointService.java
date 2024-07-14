package teamseven.echoeco.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.repository.UserPointRepository;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointRepository userPointRepository;

    public UserPoint findByUser(User user) {
        return userPointRepository.findByUser(user);
    }

    // userPoint 업데이트 서비스, 이 서비스롤 addUserPoint , minusUserPoint 두개로 나눌 필요 있을지?
    public UserPoint updateUserPoint(User user, int updatePoint) {
        UserPoint userPoint = findByUser(user);
        userPoint.updatePoint(updatePoint);
        userPointRepository.save(userPoint);
        return userPoint;
    }
}
