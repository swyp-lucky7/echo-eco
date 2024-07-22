package teamseven.echoeco.video.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import teamseven.echoeco.config.QuerydslConfiguration;
import teamseven.echoeco.config.exception.NoRemainVideoException;
import teamseven.echoeco.question.domain.ContentUserCount;
import teamseven.echoeco.question.repository.ContentUserCountRepository;
import teamseven.echoeco.user.domain.Role;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.user.repository.UserRepository;
import teamseven.echoeco.video.domain.Video;
import teamseven.echoeco.video.domain.dto.VideoEndDto;
import teamseven.echoeco.video.domain.dto.VideoRequest;
import teamseven.echoeco.video.domain.dto.VideoResponse;
import teamseven.echoeco.video.repository.VideoRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class VideoServiceTest {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ContentUserCountRepository contentUserCountRepository;
    @Autowired
    private UserRepository userRepository;

    private VideoService videoService;

    @BeforeEach
    void setUp() {
        videoService = new VideoService(videoRepository, contentUserCountRepository);
    }

    @Test
    @DisplayName("엔티티를 저장할 수 있어야 한다.")
    public void givenEntity_whenCreate_thenSave() {
        //given
        Video video = Video.builder()
                .name("해수면 상승")
                .url("http://")
                .build();

        //when
        videoService.save(video);

        //then
        List<Video> all = videoRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(video.getId(), all.get(0).getId());
    }

    @Test
    @DisplayName("video update 요청이 오면 업데이트 되어야 한다.")
    public void givenVideo_whenUpdateVideo_thenVideoUpdated() {
        // given
        Video video = Video.builder()
                .name("영상")
                .url("http://")
                .build();
        videoService.save(video);

        VideoRequest videoRequest = VideoRequest.builder()
                .name("영상2")
                .url("http://2")
                .build();
        videoService.update(video.getId(), videoRequest);

        //when
        Video updatedVideo = videoService.findById(video.getId());

        //then
        assertEquals("영상2", updatedVideo.getName());
        assertEquals("http://2", updatedVideo.getUrl());
    }

    @Test
    @DisplayName("이미 횟수를 소진한 유저가 video 요청이 에러를 반환해야 한다.")
    void givenAlreadyUseVideo_whenGetVideo_thenError() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Video video1 = Video.builder().name("영상").url("http://").build();
        videoRepository.save(video1);

        Video video2= Video.builder().name("영상2").url("http://2").build();
        videoRepository.save(video2);

        ContentUserCount contentUserCount = ContentUserCount.builder().user(user).remainQuestionCount(0).remainVideoCount(0).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        Exception exception = assertThrows(NoRemainVideoException.class, () -> videoService.video(user));

        //then
        assertEquals("오늘의 영상 횟수가 소진되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("첫 유저가 video 요청이 하면 가진 영상중에 랜덤으로 줘야 한다.")
    void givenFirstUser_whenGetVideo_thenSuccess() throws NoRemainVideoException {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Video video1 = Video.builder()
                .name("영상")
                .url("http://")
                .build();
        videoRepository.save(video1);

        Video video2= Video.builder()
                .name("영상2")
                .url("http://2")
                .build();
        videoRepository.save(video2);

        //when
        VideoResponse video = videoService.video(user);

        //then
        assertNotNull(video);
    }


    @Test
    @DisplayName("어제 소진한 유저가 오늘 video 요청이 성공적으로 줘야 한다.")
    void givenYesterdayUse_whenGetVideo_thenSuccess() throws NoRemainVideoException {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Video video1 = Video.builder()
                .name("영상")
                .url("http://")
                .build();
        videoRepository.save(video1);

        Video video2= Video.builder()
                .name("영상2")
                .url("http://2")
                .build();
        videoRepository.save(video2);

        ContentUserCount contentUserCount = ContentUserCount.builder().updatedAt(LocalDate.now().minusDays(1)).user(user).remainQuestionCount(0).remainVideoCount(0).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        VideoResponse video = videoService.video(user);

        //then
        assertNotNull(video);
    }

    @Test
    @DisplayName("이미 횟수를 소진한 유저가 video end 를 보내면 에러를 반환해야 한다.")
    void givenAlreadyUseVideo_whenVideoEnd_thenError() {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Video video1 = Video.builder().name("영상").url("http://").build();
        videoRepository.save(video1);

        Video video2= Video.builder().name("영상2").url("http://2").build();
        videoRepository.save(video2);

        ContentUserCount contentUserCount = ContentUserCount.builder().user(user).remainQuestionCount(0).remainVideoCount(0).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        Exception exception = assertThrows(NoRemainVideoException.class, () -> videoService.videoEnd(user));

        //then
        assertEquals("오늘의 영상 횟수가 소진되었습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("정상적으로 비디오를 다 본 경우 문제의 횟수를 다시 리셋 시켜준다.")
    void givenWatchedVideo_whenVideoEnd_thenResetQuestionCount() throws NoRemainVideoException {
        User user = new User("name1", "email1@aaa.com", "aa", Role.USER);
        userRepository.save(user);

        Video video1 = Video.builder().name("영상").url("http://").build();
        videoRepository.save(video1);

        Video video2= Video.builder().name("영상2").url("http://2").build();
        videoRepository.save(video2);

        ContentUserCount contentUserCount = ContentUserCount.builder().user(user).remainQuestionCount(0).remainVideoCount(1).build();
        contentUserCountRepository.save(contentUserCount);

        //when
        VideoEndDto videoEndDto = videoService.videoEnd(user);

        //then
        assertEquals(0, contentUserCount.getRemainVideoCount());
        assertEquals(3, contentUserCount.getRemainQuestionCount());
    }
}
