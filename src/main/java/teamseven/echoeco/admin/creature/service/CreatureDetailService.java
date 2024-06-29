package teamseven.echoeco.admin.creature.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.admin.creature.domain.CreatureDetail;
import teamseven.echoeco.admin.creature.repository.CreatureDetailRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatureDetailService {
    private final CreatureDetailRepository creatureDetailRepository;

    public void save(CreatureDetail creatureDetail) {
        creatureDetailRepository.save(creatureDetail);
    }
}
