package com.gdsc.blended.common.image.service;

import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.common.image.repository.ImageRepository;
import com.gdsc.blended.common.image.exception.BusinessLogicException;
import com.gdsc.blended.common.image.exception.ExceptionCode;
import com.gdsc.blended.post.entity.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3Client s3Client;
    private final S3UploadService s3UploadService;


    public ImageEntity createImage(String imagePath, PostEntity post) {
        ImageEntity image = ImageEntity.builder()
                .post(post)
                .path(imagePath)
                .build();
        return imageRepository.save(image);
    }

    public String findImageByPostId(Long postId) {
        ImageEntity entity = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
        return entity.getPath();
    }

    public ImageEntity findImageByPostId(Long postId) {
        return imageRepository.findByPostId(postId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
    }

    public void deleteImage(ImageEntity image) {
        String path = image.getPath();

        s3UploadService.delete(path);
        imageRepository.delete(image);
    }
}

    /*public void removeImage(Long imageId) {
        ImageEntity findPicture = findVerifiedImage(imageId);
        imageRepository.delete(findPicture);
    }

    private ImageEntity findVerifiedImage(Long imageId) {
        Optional<ImageEntity> findImage = imageRepository.findById(imageId);
        return findImage.orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
    }*/

    /*private ImageEntity findVerifiedPath(String path) {
        Optional<ImageEntity> findImage = imageRepository.findByPath(path);
        return findImage.orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
    }*/

