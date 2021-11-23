package com.programming.techie.service;

import com.programming.techie.dto.UploadVideoResponse;
import com.programming.techie.model.Video;
import com.programming.techie.model.VidoeDto;
import com.programming.techie.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

    public UploadVideoResponse uploadVideo(MultipartFile file) {
        String videoUrl = s3Service.uploadFile(file);
        var video = new Video();
        video.setVideoUrl(videoUrl);

        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(videoUrl, savedVideo.getVideoUrl());

    }

    public VidoeDto editVideo(VidoeDto videoDto) {
        // Find the video by videoId
        var savedVideo = getVideoById(videoDto.getId());

        // Map the videoDto fields to savedVideo
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());

        // save the video to database
        videoRepository.save(savedVideo);
        return videoDto;
    }

    private Video getVideoById(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find video by id -" + videoId));
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        // Find the video by videoId
        var savedVideo = getVideoById(videoId);

        // upload thumbnail to S3
        var thumbnailUrl = s3Service.uploadFile(file);
        savedVideo.setThumbnailUrl(thumbnailUrl);

        // save the video to database
        videoRepository.save(savedVideo);
        return thumbnailUrl;
    }
}
