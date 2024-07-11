package teamseven.echoeco.item.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.item.domain.Item;
import teamseven.echoeco.item.repository.ItemRepository;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
}
