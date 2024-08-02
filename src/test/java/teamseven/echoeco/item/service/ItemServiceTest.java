package teamseven.echoeco.item.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.background.repository.BackgroundRepository;
import teamseven.echoeco.background.service.BackgroundService;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterType;
import teamseven.echoeco.character.repository.CharacterDetailRepository;
import teamseven.echoeco.character.repository.CharacterRepository;
import teamseven.echoeco.character.repository.CharacterUserRepository;
import teamseven.echoeco.character.service.CharacterDetailService;
import teamseven.echoeco.character.service.CharacterService;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.config.exception.NotAdminSettingException;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.item.domain.Item;
import teamseven.echoeco.item.domain.dto.ItemClickResponse;
import teamseven.echoeco.item.domain.dto.ItemDto;
import teamseven.echoeco.item.domain.dto.ItemPickResponse;
import teamseven.echoeco.item.domain.dto.ItemResponse;
import teamseven.echoeco.item.repository.ItemRepository;
import teamseven.echoeco.trash.repository.TrashUserRepository;
import teamseven.echoeco.trash.service.TrashUserService;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.domain.UserPoint;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.user.service.UserPointService;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ItemServiceTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserPointRepository userPointRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private CharacterUserRepository characterUserRepository;
    @Autowired
    private BackgroundRepository backgroundRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CharacterDetailRepository characterDetailRepository;
    @Autowired
    private TrashUserRepository trashUserRepository;

    private ItemService itemService;
    private UserPointService userPointService;
    private CharacterService characterService;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        userPointService = new UserPointService(userPointRepository);
        BackgroundService backgroundService = new BackgroundService(backgroundRepository);
        CharacterDetailService characterDetailService = new CharacterDetailService(characterDetailRepository);
        TrashUserService trashUserService = new TrashUserService(trashUserRepository, userPointService);
        characterService = new CharacterService(characterRepository, characterUserRepository,
                characterDetailService, backgroundService, trashUserService, userPointService);
        itemService = new ItemService(itemRepository, userPointRepository, userPointService, characterService);
        item1 = Item.builder()
                .name("item1")
                .price(100)
                .description("item1")
                .levelUp(1)
                .isUse(true)
                .imageUrl("item1").build();

        item2 = Item.builder()
                .name("item2")
                .price(200)
                .description("item2")
                .levelUp(2)
                .isUse(false)
                .imageUrl("item2").build();
    }

    @Test
    @DisplayName("아이템 저장 테스트 아이템 저장 후 아이디로 찾으면 같은 이름의 아이템이 찾아진다")
    void saveAndFindTest() {
        itemService.saveItem(item1);
        System.out.println("itemId1::" + item1.getId());
        Item foundItem = itemService.findById(item1.getId());
        assertThat(foundItem.getName()).isEqualTo(item1.getName());
    }

    @Test
    @DisplayName("아이템 2개 저장 후 모두 찾으면 2개의 아이템 리스트가 찾아진다.")
    void findAllTest() {
        itemService.saveItem(item1);
        itemService.saveItem(item2);
        System.out.println("itemId2::" + item1.getId());

        List<Item> allItems = itemService.findAllItems();
        assertThat(allItems.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("아이템 저장 후 아이템 아이디로 삭제후 아이템을 찾을시 NoSuchElementException 이 발생한다.")
    void deleteTest() {
        itemService.saveItem(item1);
        itemService.deleteById(item1.getId());

        Assertions.assertThatThrownBy(() -> itemService.findById(item1.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("아이템 업데이트시 새로운 이름으로 업데이트된다.")
    void updateTest() {
        itemService.saveItem(item1);

        ItemDto itemDto = new ItemDto();
        String newName = "newName";
        itemDto.setName(newName);

        itemService.updateItem(item1.getId(), itemDto);
        Item foundItem = itemService.findById(item1.getId());
        assertThat(foundItem.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("isUse 가 true 인 아이템 1개, false 인 아이템 1개를 저장후 findByIsUse 로 찾으면 리스트에 1개의 아이템만 존재한다")
    void findByIsUseTest() {
        Item trueItem = Item.builder()
                .isUse(true).price(1).levelUp(1).imageUrl("true").name("true").build();

        Item falseItem = Item.builder()
                .isUse(false).price(1).levelUp(1).imageUrl("false").name("false").build();
        itemService.saveItem(trueItem);
        itemService.saveItem(falseItem);

        List<ItemResponse> allItems = itemService.findByIsUse();
        assertThat(allItems.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("구매 포인트가 충분한 상태에서 아이템 클릭하면 구매 가능 여부(true) 와 현재 유저 포인트를 반환한다.")
    void clickTest1() {
        User user = saveAndGetUser(item1.getPrice());
        itemService.saveItem(item1);

        ItemClickResponse itemClickResponse = itemService.clickItem(item1.getId(), user);

        assertThat(itemClickResponse.isAvailableBuy()).isTrue();
        assertThat(itemClickResponse.getUserPoint()).isEqualTo(100);
    }

    @Test
    @DisplayName("구매 포인트가 부족한 상태에서 아이템 클릭하면 구매 가능 여부(false) 와 현재 유저 포인트를 반환한다.")
    void clickTest2() {
        User user = saveAndGetUser(item1.getPrice() - 1);
        itemService.saveItem(item1);

        ItemClickResponse itemClickResponse = itemService.clickItem(item1.getId(), user);

        assertThat(itemClickResponse.isAvailableBuy()).isFalse();
        assertThat(itemClickResponse.getUserPoint()).isEqualTo(item1.getPrice() - 1);
    }

    @Test
    @DisplayName("구매 포인트가 충분한 상태에서 아이템을 픽하면 상승한 유저의 레벨과 구매후 유저 포인트, 구매한 아이템 정보를 반환한다")
    void pickTest() throws NotAdminSettingException, NotFoundCharacterUserException {
        User user = saveAndGetUser(item1.getPrice());
        itemService.saveItem(item1);
        Character character = Character.builder().image("test").type(CharacterType.ANIMAL).descriptions("test").isPossible(true).maxLevel(100).completeMessages("test").name("test").build();
        characterService.save(character);
        characterService.pick(character.getId(), user);

        ItemPickResponse itemPickResponse = itemService.pickItem(item1.getId(), user);

        assertThat(itemPickResponse.getLevel()).isEqualTo(characterUserRepository.findByUserWithUse(user).getLevel());
        assertThat(itemPickResponse.getUserPoint()).isEqualTo(0);
        assertThat(itemPickResponse.getItemResponse().getName()).isEqualTo(item1.getName());
    }

    @Test
    @DisplayName("구매할 포인트가 부족한 상태에서 아이템을 픽하면 IllegalArgumentException 이 발생한다.")
    void pickTest2() throws NotAdminSettingException, NotFoundCharacterUserException {
        User user = saveAndGetUser(item1.getPrice() - 1);
        itemService.saveItem(item1);
        characterService.save(Character.builder().id(1l).image("test").type(CharacterType.ANIMAL).descriptions("test").isPossible(true).maxLevel(100).completeMessages("test").name("test").build());
        characterService.pick(1l, user);

        Assertions.assertThatThrownBy(() -> itemService.pickItem(item1.getId(), user)).isInstanceOf(IllegalArgumentException.class);
    }

    private User saveAndGetUser(int userPoint) {
        User user = User.builder()
                .email("test")
                .name("test")
                .role(Role.USER)
                .picture("test")
                .build();
        userRepository.save(user);
        userPointRepository.save(UserPoint.fromUser(user));
        userPointService.addUserPoint(user,userPoint);
        return user;
    }
}