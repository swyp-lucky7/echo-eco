package teamseven.echoeco.video.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.config.exception.NoRemainVideoException;
import teamseven.echoeco.question.domain.ContentUserCount;
import teamseven.echoeco.question.repository.ContentUserCountRepository;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.video.domain.Video;
import teamseven.echoeco.video.domain.dto.VideoEndDto;
import teamseven.echoeco.video.domain.dto.VideoRequest;
import teamseven.echoeco.video.domain.dto.VideoResponse;
import teamseven.echoeco.video.repository.VideoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final ContentUserCountRepository contentUserCountRepository;

    public void save(Video video) {
        videoRepository.save(video);
    }

    public Video findById(Long id) {
        return videoRepository.findById(id).orElseThrow();
    }

    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    public void delete(Long id) {
        videoRepository.deleteById(id);
    }

    public Video update(Long id, VideoRequest videoRequest) {
        Video video = videoRepository.findById(id).orElseThrow();
        video.update(videoRequest);
        save(video);
        return video;
    }

    public VideoResponse video(User user) throws NoRemainVideoException {
        Optional<ContentUserCount> contentCountOptional = contentUserCountRepository.findByUser_Id(user.getId());
        ContentUserCount contentUserCount = contentCountOptional.orElseGet(() -> ContentUserCount.create(user));

        if (!contentUserCount.getResetAt().equals(LocalDate.now())) {
            contentUserCount.reset();
        }

        if (contentUserCount.getRemainVideoCount() <= 0) {
            throw new NoRemainVideoException();
        }
        contentUserCountRepository.save(contentUserCount);

        Random random = new Random();
        List<Long> allIds = videoRepository.findAllIds();
        long randomIndex = allIds.get(random.nextInt(allIds.size()));
        Video video = videoRepository.findById(randomIndex).orElseThrow();

        return VideoResponse.fromEntity(video);
    }

    public VideoEndDto videoEnd(User user) throws NoRemainVideoException {
        Optional<ContentUserCount> contentCountOptional = contentUserCountRepository.findByUser_Id(user.getId());
        ContentUserCount contentUserCount = contentCountOptional.orElseThrow(() -> new IllegalCallerException("비디오를 한번도 요청하지 않은 유저입니다."));

        if (contentUserCount.getRemainVideoCount() <= 0) {
            throw new NoRemainVideoException();
        }

        contentUserCount.watchedVideo();
        return VideoEndDto.makeVideoEndDto();
    }
}
