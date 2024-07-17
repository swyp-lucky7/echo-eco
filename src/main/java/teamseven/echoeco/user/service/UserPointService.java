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

    public UserPoint subtractUserPoint(User user, int updatePoint) {
        UserPoint userPoint = findByUser(user);
        if (userPoint.getUserPoint() < updatePoint) {
            throw new IllegalArgumentException("구매를 진행힐 포인트가 부족합니다.");
        }
        userPoint.subtractPoint(updatePoint);
        userPointRepository.save(userPoint);
        return userPoint;
    }

    public UserPoint addUserPoint(User user, int updatePoint) {
        UserPoint userPoint = findByUser(user);
        userPoint.addPoint(updatePoint);
        userPointRepository.save(userPoint);
        return userPoint;
    }
}
