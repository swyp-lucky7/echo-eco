package teamseven.echoeco.admin.creature.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.admin.creature.domain.Creature;
import teamseven.echoeco.admin.creature.repository.CreatureDetailRepository;
import teamseven.echoeco.admin.creature.repository.CreatureRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatureService {
    private final CreatureRepository creatureRepository;

    public void save(Creature creature) {
        creatureRepository.save(creature);
    }

    public List<Creature> findAll() {
        return creatureRepository.findAll();
    }
}
