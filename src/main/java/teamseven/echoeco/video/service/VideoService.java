package teamseven.echoeco.video.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamseven.echoeco.question.domain.QuestionUserCount;
import teamseven.echoeco.question.repository.QuestionUserCountRepository;
import teamseven.echoeco.user.domain.User;
import teamseven.echoeco.video.domain.Video;
import teamseven.echoeco.video.domain.dto.VideoEndDto;
import teamseven.echoeco.video.domain.dto.VideoRequest;
import teamseven.echoeco.video.domain.dto.VideoResponse;
import teamseven.echoeco.video.repository.VideoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final QuestionUserCountRepository questionUserCountRepository;

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

    public VideoResponse video() {
        Random random = new Random();
        long count = videoRepository.count();
        long randomIndex = random.nextLong(count);
        Video video = videoRepository.findById(randomIndex).orElseThrow();
        return VideoResponse.fromEntity(video);
    }

    public VideoEndDto videoEnd(User user) {
        Optional<QuestionUserCount> questionUserCountOptional = questionUserCountRepository.findByUser_Id(user.getId());
        QuestionUserCount questionUserCount = questionUserCountOptional.orElseGet(QuestionUserCount::create);
        questionUserCount.fillRemainQuestionCount();

        return VideoEndDto.makeVideoEndDto();
    }
}
