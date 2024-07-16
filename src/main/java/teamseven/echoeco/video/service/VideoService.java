package teamseven.echoeco.video.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamseven.echoeco.video.domain.Video;
import teamseven.echoeco.video.domain.dto.VideoRequest;
import teamseven.echoeco.video.repository.VideoRepository;

import java.util.List;

@Service
@Repository
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;

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
}
