package teamseven.echoeco.background.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.background.domain.TrashUser;
import teamseven.echoeco.background.repository.TrashUserRepository;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.user.domain.User;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TrashUserService {
    private final TrashUserRepository trashUserRepository;

    public void save(TrashUser trashUser) {
        trashUserRepository.save(trashUser);
    }

    public boolean isClearTheTrash(User user) {
        Optional<TrashUser> optionalTrashUser = trashUserRepository.findByUser_Id(user.getId());
        if (optionalTrashUser.isEmpty()) {
            return false;
        }
        TrashUser trashUser = optionalTrashUser.get();
        LocalDate updateDate = trashUser.getUpdated_at().toLocalDate();
        LocalDate today = LocalDate.now();
        return updateDate.isEqual(today);
    }

    public Environment getEnvironment(User user) {
        if (isClearTheTrash(user)) {
            return Environment.CLEAN;
        }
        return Environment.TRASH;
    }
}
