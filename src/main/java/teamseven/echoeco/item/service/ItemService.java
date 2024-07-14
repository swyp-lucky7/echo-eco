package teamseven.echoeco.item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.item.domain.Item;
import teamseven.echoeco.item.domain.dto.ItemClickResponse;
import teamseven.echoeco.item.domain.dto.ItemDto;
import teamseven.echoeco.item.domain.dto.ItemResponse;
import teamseven.echoeco.item.repository.ItemRepository;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.repository.UserPointRepository;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserPointRepository userPointRepository;

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

    public Item updateItem(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id).orElseThrow();
        item.updateByItemDto(itemDto);
        saveItem(item);
        return item;
    }

    public List<ItemResponse> findByIsUse() {
        List<ItemResponse> itemResponses = new ArrayList<>();
        List<Item> byIsUse = itemRepository.findByIsUse(true);
        for (Item item : byIsUse) {
            ItemResponse itemResponse = ItemResponse.fromEntity(item);
            itemResponses.add(itemResponse);
        }
        return itemResponses;
    }

    public ItemClickResponse itemClickResponse(Long itemId, User user) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템 아이디 입니다."));

        ItemResponse itemResponse = ItemResponse.fromEntity(item);
        UserPoint userPointEntity = userPointRepository.findByUser(user);
        int userPoint = userPointEntity.getUserPoint();
        boolean available_buy = userPoint >= item.getPrice();

        return ItemClickResponse.makeItemClickResponse(itemResponse, userPoint, available_buy);
    }
}
