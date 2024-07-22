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
import teamseven.echoeco.question.repository.QuestionUserCountRepository;
import teamseven.echoeco.video.domain.Video;
import teamseven.echoeco.video.domain.dto.VideoRequest;
import teamseven.echoeco.video.repository.VideoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class VideoServiceTest {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private QuestionUserCountRepository questionUserCountRepository;

    private VideoService videoService;

    @BeforeEach
    void setUp() { videoService = new VideoService(videoRepository, questionUserCountRepository); }

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
}
