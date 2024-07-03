package teamseven.echoeco.trash.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.trash.domain.TrashUser;
import teamseven.echoeco.trash.repository.TrashUserRepository;
import teamseven.echoeco.character.domain.Environment;
import teamseven.echoeco.config.exception.AlreadyCleanTrashException;
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

    public boolean isTodayCleanTrash(User user) {
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
        if (isTodayCleanTrash(user)) {
            return Environment.CLEAN;
        }
        return Environment.TRASH;
    }

    public void cleanTrash(User user) throws AlreadyCleanTrashException {
        if (isTodayCleanTrash(user)) {
            throw new AlreadyCleanTrashException();
        }
        Optional<TrashUser> optionalTrashUser = trashUserRepository.findByUser_Id(user.getId());
        TrashUser trashUser = optionalTrashUser.orElseGet(() -> TrashUser.builder()
                .user(user)
                .build());

        trashUserRepository.save(trashUser);
    }
}