package teamseven.echoeco.item.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.character.service.CharacterService;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.item.domain.Item;
import teamseven.echoeco.item.domain.dto.ItemClickResponse;
import teamseven.echoeco.item.domain.dto.ItemDto;
import teamseven.echoeco.item.domain.dto.ItemPickResponse;
import teamseven.echoeco.item.domain.dto.ItemResponse;
import teamseven.echoeco.item.repository.ItemRepository;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.service.UserPointService;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserPointRepository userPointRepository;
    private final UserPointService userPointService;
    private final CharacterService characterService;

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

    public ItemClickResponse clickItem(Long itemId, User user) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템 아이디 입니다."));

        ItemResponse itemResponse = ItemResponse.fromEntity(item);
        UserPoint userPointEntity = userPointRepository.findByUser(user);
        int userPoint = userPointEntity.getUserPoint();
        boolean available_buy = userPoint >= item.getPrice();

        return ItemClickResponse.makeItemClickResponse(itemResponse, userPoint, available_buy);
    }

    public ItemPickResponse pickItem(Long itemId, User user) throws NotFoundCharacterUserException {
        Item userItem = this.findById(itemId);
        UserPoint userPoint = userPointService.subtractUserPoint(user, userItem.getPrice());
        CharacterUser characterUser = characterService.addUserCharacterLevel(user, userItem.getLevelUp());
        ItemResponse itemResponse = ItemResponse.fromEntity(userItem);
        return ItemPickResponse.makeItemPickResponse(itemResponse, userPoint.getUserPoint(), characterUser.getLevel());
    }

}
