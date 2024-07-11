package teamseven.echoeco.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import teamseven.echoeco.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
