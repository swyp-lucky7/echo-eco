package teamseven.echoeco.gifticon.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import teamseven.echoeco.character.domain.Character;
import teamseven.echoeco.character.domain.CharacterUser;
import teamseven.echoeco.gifticon.domain.GifticonUser;
import teamseven.echoeco.user.domain.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static teamseven.echoeco.config.Constants.DEFAULT_IMAGE_URL;

@Data
@Builder
@AllArgsConstructor
public class GifticonDetailResponse {
    private String userName;
    private String userEmail;

    private String characterName;
    private int characterMaxLevel;
    private int characterNowLevel;

    private String characterUserCreatedAt;
    private long periodOfRaising;

    private String gifticonName;
    private String imageUrl;
    private Boolean isSend;
    private String email;
    private String sendAdminName;
    private String sentAt;
    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void calculatePeriod() {
        LocalDateTime create = LocalDateTime.parse(createdAt, formatter);
        LocalDateTime characterUserCreate = LocalDateTime.parse(characterUserCreatedAt, formatter);

        Duration between = Duration.between(characterUserCreate, create);
        periodOfRaising = between.toDays();
    }

    public static GifticonDetailResponse create(GifticonUser gifticonUser) {
        User user = gifticonUser.getUser();
        CharacterUser characterUser = gifticonUser.getCharacterUser();
        Character character = characterUser.getCharacter();

        GifticonDetailResponse response;
        if (gifticonUser.getIsSend()) {
            response = GifticonDetailResponse.builder()
                    .userName(user.getName())
                    .userEmail(user.getEmail())
                    .characterName(character.getName())
                    .characterMaxLevel(character.getMaxLevel())
                    .characterNowLevel(characterUser.getLevel())
                    .characterUserCreatedAt(characterUser.getCreatedAt().format(formatter))
                    .gifticonName(gifticonUser.getName())
                    .imageUrl(gifticonUser.getImageUrl())
                    .isSend(gifticonUser.getIsSend())
                    .email(gifticonUser.getEmail())
                    .sendAdminName(gifticonUser.getSendAdminName())
                    .sentAt(gifticonUser.getSentAt().format(formatter))
                    .createdAt(gifticonUser.getCreatedAt().format(formatter))
                    .updatedAt(gifticonUser.getUpdatedAt().format(formatter))
                    .build();
        } else {
            response = GifticonDetailResponse.builder()
                    .userName(user.getName())
                    .userEmail(user.getEmail())
                    .characterName(character.getName())
                    .characterMaxLevel(character.getMaxLevel())
                    .characterNowLevel(characterUser.getLevel())
                    .characterUserCreatedAt(characterUser.getCreatedAt().format(formatter))
                    .gifticonName("")
                    .imageUrl(DEFAULT_IMAGE_URL)
                    .isSend(gifticonUser.getIsSend())
                    .email(gifticonUser.getEmail())
                    .sendAdminName("")
                    .sentAt("")
                    .createdAt(gifticonUser.getCreatedAt().format(formatter))
                    .updatedAt(gifticonUser.getUpdatedAt().format(formatter))
                    .build();
        }


        response.calculatePeriod();
        return response;
    }
}
