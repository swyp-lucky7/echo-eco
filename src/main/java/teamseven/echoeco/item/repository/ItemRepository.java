package teamseven.echoeco.item.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import teamseven.echoeco.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByIsUse(boolean isUse);
}
