package teamseven.echoeco.gifticon.service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.character.repository.CharacterUserRepository;
import teamseven.echoeco.config.exception.NotFoundCharacterUserException;
import teamseven.echoeco.file.service.FileService;
import teamseven.echoeco.gifticon.domain.GifticonUser;
import teamseven.echoeco.gifticon.domain.dto.GifticonCheckResponse;
import teamseven.echoeco.gifticon.domain.dto.GifticonDetailResponse;
import teamseven.echoeco.gifticon.domain.dto.GifticonUserAdminResponse;
import teamseven.echoeco.gifticon.repository.GifticonRepository;
import teamseven.echoeco.mail.service.MailService;
import teamseven.echoeco.user.domain.User;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GifticonService {
    private final GifticonRepository gifticonRepository;
    private final MailService mailService;
    private final CharacterUserRepository characterUserRepository;
    private final FileService fileService;

    public List<GifticonUserAdminResponse> search(String userEmail, Boolean isSend) {
        return gifticonRepository.search(userEmail, isSend);
    }

    public void send(Long id, User admin, MultipartFile multipartFile) throws MessagingException {
        Optional<GifticonUser> byId = gifticonRepository.findById(id);
        GifticonUser gifticonUser = byId.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id 입니다."));

        if (gifticonUser.getIsSend()) {
            throw new IllegalArgumentException("해당 유저는 이미 받은 유저입니다.");
        }
        File file = fileService.convert(multipartFile);
        mailService.sendEmailWithAttachment(gifticonUser.getEmail(), "Echoeco 기프티콘", getEmailBody(gifticonUser.getName()), file);

        String url = fileService.upload(multipartFile);

        gifticonUser.sendGift(url, admin);
        gifticonRepository.save(gifticonUser);
    }

    public GifticonDetailResponse detail(Long id) {
        Optional<GifticonUser> byId = gifticonRepository.findById(id);
        GifticonUser gifticonUser = byId.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id 입니다."));

        return GifticonDetailResponse.create(gifticonUser);
    }

    public void post(String email, User user) throws NotFoundCharacterUserException {
        CharacterUser characterUser = characterUserRepository.findByUserWithUse(user);
        if (characterUser == null) {
            throw new NotFoundCharacterUserException();
        }
        Character character = characterUser.getCharacter();
        if (characterUser.getLevel() < character.getMaxLevel()) {
            throw new IllegalCallerException("해당 유저의 레벨이 만렙보다 작습니다.");
        }

        GifticonUser gifticonUser = GifticonUser.builder()
                .user(user)
                .characterUser(characterUser)
                .email(email)
                .build();

        gifticonRepository.save(gifticonUser);
    }

    public GifticonCheckResponse alreadyExistCheck(User user) throws NotFoundCharacterUserException {
        CharacterUser characterUser = characterUserRepository.findByUserWithUse(user);
        if (characterUser == null) {
            throw new NotFoundCharacterUserException();
        }

        Optional<GifticonUser> byCharacterUser = gifticonRepository.findByCharacterUser(characterUser);
        return GifticonCheckResponse.builder()
                .isPost(byCharacterUser.isPresent())
                .build();
    }

    private String getEmailBody(String productName) {
        return "선택하신 동물을 끝까지 책임지고 키워주셔서 감사합니다! 감사의 마음을 담아 " + productName + " 기프티콘을 선물 드립니다. \n\n\n";
    }

}
