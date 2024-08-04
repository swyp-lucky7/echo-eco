package teamseven.echoeco.gifticon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterType;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.character.repository.CharacterRepository;
import teamseven.echoeco.character.repository.CharacterUserRepository;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.file.service.FileService;
import teamseven.echoeco.gifticon.domain.GifticonUser;
import teamseven.echoeco.gifticon.domain.dto.GifticonCheckResponse;
import teamseven.echoeco.gifticon.domain.dto.GifticonDetailResponse;
import teamseven.echoeco.gifticon.domain.dto.GifticonUserAdminResponse;
import teamseven.echoeco.gifticon.repository.GifticonRepository;
import teamseven.echoeco.mail.service.MailService;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private CharacterUserRepository characterUserRepository;
    @Autowired
    private CharacterRepository characterRepository;

    private GifticonService gifticonService;

    private MailService mailService;
    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private FileService fileService;

    private MockMultipartFile multipartFile;

    @BeforeEach
    void setUp() throws MalformedURLException {
        MimeMessage mimeMessage = new MimeMessage((Session)null);
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

        mailService = new MailService(javaMailSender);
        gifticonService = new GifticonService(gifticonRepository, mailService, characterUserRepository, fileService);

        when(amazonS3.getUrl(any(), any())).thenReturn(new URL("http://mock-s3-url/test.txt"));
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
        List<GifticonUserAdminResponse> search = gifticonService.search(user.getEmail(), null);

        //then
        assertEquals(1, search.size());
        assertEquals(gifticonUser1.getId(), search.get(0).getId());
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
        assertEquals(gifticonUser1.getId(), search.get(0).getId());
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
    void givenNoCorrectIdCondition_whenSend_thenError() throws IOException {
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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gifticonService.send(gifticonUser.getId() + 100L, admin, multipartFile));

        //then
        assertEquals("존재하지 않는 id 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("운영자가 send 했을때 이미 받았으면 에러가 발생해야 한다.")
    void givenNoCorrectCondition_whenSend_thenError() throws IOException {
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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gifticonService.send(gifticonUser.getId(), admin, multipartFile));

        //then
        assertEquals("해당 유저는 이미 받은 유저입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("운영자가 send 했을때 정상적으로 저장이 잘되어야 한다.")
    void givenCorrectCondition_whenSend_thenSuccess() throws MessagingException, IOException {
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
        gifticonService.send(gifticonUser.getId(), admin, multipartFile);

        //then
        assertEquals(true, gifticonUser.getIsSend());
        assertEquals(admin.getName(), gifticonUser.getSendAdminName());
        assertEquals("http://mock-s3-url/test.txt", gifticonUser.getImageUrl());
    }

    @Test
    @DisplayName("유저가 post 할때 사용하고 있는 캐릭터가 없으면 에러를 반환해야 한다.")
    void givenNoCharacterUser_whenPost_thenError() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);
        saveDefaultCharacter();

        //when
        Exception exception = assertThrows(NotFoundCharacterUserException.class, () -> gifticonService.post(user.getEmail(), user));

        //then
        assertEquals("해당 유저가 사용하고 있는 캐릭터가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("유저가 post 할때 사용하고 있는 캐릭터의 레벨이 만렙보다 작으면 에러를 반환해야 한다.")
    void givenCharacterLevelLowThenMaxLevel_whenPost_thenError() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);
        List<Character> characters = saveDefaultCharacter();
        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(characters.get(0).getMaxLevel() - 1).build();
        characterUserRepository.save(characterUser1);

        //when
        Exception exception = assertThrows(IllegalCallerException.class, () -> gifticonService.post(user.getEmail(), user));

        //then
        assertEquals("해당 유저의 레벨이 만렙보다 작습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("유저가 post 하면 잘 저장이 되어야 한다.")
    void givenCorrectCondition_whenPost_thenSave() throws NotFoundCharacterUserException {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);
        List<Character> characters = saveDefaultCharacter();
        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(characters.get(0).getMaxLevel()).build();
        characterUserRepository.save(characterUser1);

        //when
        gifticonService.post("aa@aaa.com", user);
        Optional<GifticonUser> byCharacterUser = gifticonRepository.findByCharacterUser(characterUser1);
        GifticonUser gifticonUser = byCharacterUser.orElseGet(() -> GifticonUser.builder().build());

        //then
        assertEquals(user, gifticonUser.getUser());
        assertEquals(characterUser1, gifticonUser.getCharacterUser());
        assertEquals(false, gifticonUser.getIsSend());
        assertEquals("aa@aaa.com", gifticonUser.getEmail());
    }

    @Test
    @DisplayName("유저가 기프티콘 이메일 보냈는지 체크할때 사용하고 있는 캐릭터가 없으면 에러를 반환해야 한다.")
    void givenNoCharacterUser_whenCheck_thenError() {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);
        saveDefaultCharacter();

        //when
        Exception exception = assertThrows(NotFoundCharacterUserException.class, () -> gifticonService.alreadyExistCheck(user));

        //then
        assertEquals("해당 유저가 사용하고 있는 캐릭터가 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("유저가 기프티콘 이메일 보냈는지 체크할때 기프티콘 정보가 없으면 false 를 반환해야 한다.")
    void givenNoGiftInfo_whenCheck_thenFalse() throws NotFoundCharacterUserException {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);
        List<Character> characters = saveDefaultCharacter();
        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(characters.get(0).getMaxLevel()).build();
        characterUserRepository.save(characterUser1);

        //when
        GifticonCheckResponse gifticonCheckResponse = gifticonService.alreadyExistCheck(user);

        //then
        assertFalse(gifticonCheckResponse.getIsPost());
    }

    @Test
    @DisplayName("유저가 기프티콘 이메일 보냈는지 체크할때 기프티콘 정보가 있으면 true 를 반환해야 한다.")
    void givenGiftInfo_whenCheck_thenTrue() throws NotFoundCharacterUserException {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);
        saveDefaultCharacter();
        List<Character> characters = saveDefaultCharacter();
        CharacterUser characterUser1 = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(characters.get(0).getMaxLevel()).build();
        characterUserRepository.save(characterUser1);
        gifticonService.post(user.getEmail(), user);

        //when
        GifticonCheckResponse gifticonCheckResponse = gifticonService.alreadyExistCheck(user);

        //then
        assertTrue(gifticonCheckResponse.getIsPost());
    }

    @Test
    @DisplayName("기프티콘 detail 요청했을때 없는 데이터면 에러를 발생해야 한다.")
    void givenNoCorrectId_whenDetail_thenError() {
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gifticonService.detail(10000L));

        //then
        assertEquals("존재하지 않는 id 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("기프티콘 detail 을 요청했을 때 데이터를 잘 넘겨줘야 한다.")
    void givenCorrectId_whenDetail_thenSuccess() throws NotFoundCharacterUserException {
        User user = new User("user1", "email1", "picture1", Role.USER);
        userRepository.save(user);
        saveDefaultCharacter();
        List<Character> characters = saveDefaultCharacter();
        CharacterUser characterUser = CharacterUser.builder().user(user).character(characters.get(0)).isUse(true).level(characters.get(0).getMaxLevel()).createdAt(LocalDateTime.now().minusDays(10)).build();
        characterUserRepository.save(characterUser);
        String email = "aa@aaa.com";
        gifticonService.post(email, user);

        GifticonUser gifticonUser = gifticonRepository.findByCharacterUser(characterUser).get();

        //when
        GifticonDetailResponse detail = gifticonService.detail(gifticonUser.getId());

        //then
        assertEquals(user.getName(), detail.getUserName());
        assertEquals(10, detail.getPeriodOfRaising());
        assertEquals(email, detail.getEmail());
    }


    private List<Character> saveDefaultCharacter() {
        Character character1 = Character.builder()
                .name("볼리베어")
                .type(CharacterType.ANIMAL)
                .image("http://")
                .maxLevel(100)
                .descriptions("곰")
                .isPossible(true)
                .completeMessages("")
                .build();
        Character character2 = Character.builder()
                .name("식물")
                .type(CharacterType.PLANT)
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