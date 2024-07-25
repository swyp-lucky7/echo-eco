package teamseven.echoeco.gifticon.service;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterType;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.character.repository.CharacterRepository;
import teamseven.echoeco.character.repository.CharacterUserRepository;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.gifticon.domain.GifticonUser;
import teamseven.echoeco.gifticon.domain.dto.GifticonUserAdminResponse;
import teamseven.echoeco.gifticon.repository.GifticonRepository;
import teamseven.echoeco.mail.service.MailService;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserPointRepository;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class GifticonServiceTest {

    @Autowired
    private GifticonRepository gifticonRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPointRepository userPointRepository;
    @Autowired
    private CharacterUserRepository characterUserRepository;
    @Autowired
    private CharacterRepository characterRepository;

    private GifticonService gifticonService;

    private MailService mailService;

    @BeforeEach
    void setUp() {
        MimeMessage mimeMessage = new MimeMessage((Session)null);
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailService = new MailService(javaMailSender);
        UserService userService = new UserService(userRepository, userPointRepository);
        gifticonService = new GifticonService(gifticonRepository, mailService, userService);
    }

    @Test
    @DisplayName("아무 조건없이 검색하면 전체가 나와야 한다.")
    void givenNoCondition_whenSearch_thenAllResult() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);

        List<Character> characters = saveDefaultCharacter();

        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser1);
        CharacterUser characterUser2 = CharacterUser.builder().user(user).character(characters.get(1)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser2);

        GifticonUser gifticonUser1 = GifticonUser.builder().user(user).characterUser(characterUser1).build();
        gifticonRepository.save(gifticonUser1);
        GifticonUser gifticonUser2 = GifticonUser.builder().user(user).characterUser(characterUser2).isSend(true).build();
        gifticonRepository.save(gifticonUser2);

        //when
        List<GifticonUserAdminResponse> search = gifticonService.search(null, null);

        //then
        assertEquals(2, search.size());

        List<Long> list = search.stream().map(GifticonUserAdminResponse::getId).toList();
        assertTrue(list.contains(gifticonUser1.getId()));
        assertTrue(list.contains(gifticonUser2.getId()));
    }

    @Test
    @DisplayName("검색할때 이메일을 검색하면 이메일이 나와야 한다.")
    void givenEmailCondition_whenSearch_thenEmailResult() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);

        User admin = new User("admin1", "email2", "picture1", Role.USER);
        userRepository.save(admin);

        List<Character> characters = saveDefaultCharacter();

        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser1);
        CharacterUser characterUser2 = CharacterUser.builder().user(admin).character(characters.get(1)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser2);

        GifticonUser gifticonUser1 = GifticonUser.builder().user(user).characterUser(characterUser1).build();
        gifticonRepository.save(gifticonUser1);
        GifticonUser gifticonUser2 = GifticonUser.builder().user(admin).characterUser(characterUser2).isSend(true).build();
        gifticonRepository.save(gifticonUser2);

        //when
        List<GifticonUserAdminResponse> search = gifticonService.search("email1", null);

        //then
        assertEquals(1, search.size());
        assertEquals(user.getId(), search.get(0).getId());
    }

    @Test
    @DisplayName("검색할때 보냄 여부에서 true를 검색하면 이메일이 나와야 한다.")
    void givenIsSendCondition_whenSearch_thenIsSendTrueResult() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);

        User admin = new User("admin1", "email2", "picture1", Role.USER);
        userRepository.save(admin);

        List<Character> characters = saveDefaultCharacter();

        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser1);
        CharacterUser characterUser2 = CharacterUser.builder().user(admin).character(characters.get(1)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser2);

        GifticonUser gifticonUser1 = GifticonUser.builder().user(user).characterUser(characterUser1).build();
        gifticonRepository.save(gifticonUser1);
        GifticonUser gifticonUser2 = GifticonUser.builder().user(admin).characterUser(characterUser2).isSend(true).build();
        gifticonRepository.save(gifticonUser2);

        //when
        List<GifticonUserAdminResponse> search = gifticonService.search(null, true);

        //then
        assertEquals(1, search.size());
        assertEquals(gifticonUser2.getId(), search.get(0).getId());
    }

    @Test
    @DisplayName("검색할때 보냄 여부에서 false를 검색하면 이메일이 나와야 한다.")
    void givenIsSendCondition_whenSearch_thenIsSendFalseResult() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);

        User admin = new User("admin1", "email2", "picture1", Role.USER);
        userRepository.save(admin);

        List<Character> characters = saveDefaultCharacter();

        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser1);
        CharacterUser characterUser2 = CharacterUser.builder().user(admin).character(characters.get(1)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser2);

        GifticonUser gifticonUser1 = GifticonUser.builder().user(user).characterUser(characterUser1).build();
        gifticonRepository.save(gifticonUser1);
        GifticonUser gifticonUser2 = GifticonUser.builder().user(admin).characterUser(characterUser2).isSend(true).build();
        gifticonRepository.save(gifticonUser2);

        //when
        List<GifticonUserAdminResponse> search = gifticonService.search(null, false);

        //then
        assertEquals(1, search.size());
        assertEquals(user.getId(), search.get(0).getId());
    }

    @Test
    @DisplayName("검색할때 보냄 여부와 email 을 검색하면 이메일이 나와야 한다.")
    void givenAllCondition_whenSearch_thenCorrectResult() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);

        User admin = new User("admin1", "email2", "picture1", Role.USER);
        userRepository.save(admin);

        List<Character> characters = saveDefaultCharacter();

        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser1);
        CharacterUser characterUser2 = CharacterUser.builder().user(admin).character(characters.get(1)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser2);

        GifticonUser gifticonUser1 = GifticonUser.builder().user(user).characterUser(characterUser1).build();
        gifticonRepository.save(gifticonUser1);
        GifticonUser gifticonUser2 = GifticonUser.builder().user(admin).characterUser(characterUser2).isSend(true).build();
        gifticonRepository.save(gifticonUser2);

        //when
        List<GifticonUserAdminResponse> search = gifticonService.search("email1", false);

        //then
        assertEquals(1, search.size());
        assertEquals(gifticonUser1.getId(), search.get(0).getId());
    }


    @Test
    @DisplayName("운영자가 send 했을때 id 가 없으면 에러가 발생해야 한다.")
    void givenNoCorrectIdCondition_whenSend_thenError() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);

        User admin = new User("admin1", "email2", "picture1", Role.ADMIN);
        userRepository.save(admin);

        List<Character> characters = saveDefaultCharacter();

        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser1);

        GifticonUser gifticonUser = GifticonUser.builder().user(user).characterUser(characterUser1).isSend(true).build();
        gifticonRepository.save(gifticonUser);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gifticonService.send(gifticonUser.getId() + 100L, "123", admin));

        //then
        assertEquals("존재하지 않는 id 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("운영자가 send 했을때 이미 받았으면 에러가 발생해야 한다.")
    void givenNoCorrectCondition_whenSend_thenError() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);

        User admin = new User("admin1", "email2", "picture1", Role.ADMIN);
        userRepository.save(admin);

        List<Character> characters = saveDefaultCharacter();

        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser1);

        GifticonUser gifticonUser = GifticonUser.builder().user(user).characterUser(characterUser1).isSend(true).build();
        gifticonRepository.save(gifticonUser);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gifticonService.send(gifticonUser.getId(), "123", admin));

        //then
        assertEquals("해당 유저는 이미 받은 유저입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("운영자가 send 했을때 정상적으로 저장이 잘되어야 한다.")
    void givenCorrectCondition_whenSend_thenSuccess() throws MessagingException {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);

        User admin = new User("admin1", "email2", "picture1", Role.ADMIN);
        userRepository.save(admin);

        List<Character> characters = saveDefaultCharacter();

        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(0).build();
        characterUserRepository.save(characterUser1);

        GifticonUser gifticonUser = GifticonUser.builder().user(user).email("email1").characterUser(characterUser1).build();
        gifticonRepository.save(gifticonUser);

        //when
        gifticonService.send(gifticonUser.getId(), "123", admin);

        //then
        assertEquals(true, gifticonUser.getIsSend());
        assertEquals(admin.getName(), gifticonUser.getSendAdminName());
        assertEquals("123", gifticonUser.getNumber());
    }



    private List<Character> saveDefaultCharacter() {
        Character character1 = Character.builder()
                .name("볼리베어")
                .type(CharacterType.ANIMAL)
                .frameImage("http://")
                .image("http://")
                .maxLevel(100)
                .descriptions("곰")
                .isPossible(true)
                .completeMessages("")
                .build();
        Character character2 = Character.builder()
                .name("식물")
                .type(CharacterType.PLANT)
                .frameImage("http://")
                .image("http://")
                .maxLevel(25)
                .descriptions("꽃")
                .isPossible(false)
                .completeMessages("")
                .build();

        characterRepository.save(character1);
        characterRepository.save(character2);
        return List.of(character1, character2);
    }
}