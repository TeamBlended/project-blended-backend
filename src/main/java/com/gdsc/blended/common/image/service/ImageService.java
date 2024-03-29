package com.gdsc.blended.common.image.service;

import com.gdsc.blended.common.message.PostResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.common.image.repository.ImageRepository;
import com.gdsc.blended.post.entity.PostEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final S3Client s3Client;
    private final S3UploadService s3UploadService;

    @Transactional
    public String uploadImage(MultipartFile multipartFile, String filePath) {
        try {
            // ImageUploadService를 이용하여 이미지 업로드 처리
            String imageUrl = s3UploadService.upload(multipartFile, filePath);

            // 이미지 정보 저장
            ImageEntity imageEntity = createImage(imageUrl);
            return imageEntity.getPath();
        } catch (IOException e) {
            throw new ApiException(PostResponseMessage.IMAGE_UPLOAD_FAILED);
        }
    }
    public ImageEntity createImage(String imagePath) {
        ImageEntity image = ImageEntity.builder()
                .path(imagePath)
                .build();
        return imageRepository.save(image);
    }

    public ImageEntity createImage(String imagePath, PostEntity post) {
        ImageEntity image = ImageEntity.builder()
                .post(post)
                .path(imagePath)
                .build();
        return imageRepository.save(image);
    }

    public String findImagePathByPostId(Long postId) {
        ImageEntity imageEntity = findImageByPostId(postId);
        return (imageEntity == null) ? null : imageEntity.getPath();
    }

    public ImageEntity findImageByPostId(Long postId) {
        ImageEntity imageEntity = imageRepository.findByPostId(postId);
        if (imageEntity == null) {
            throw new ApiException(PostResponseMessage.IMAGE_NOT_FOUND);
        }
        return imageEntity;
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

