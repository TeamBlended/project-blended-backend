package com.gdsc.blended.common.image.service;

import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.common.image.repository.ImageRepository;
import com.gdsc.blended.common.image.exception.BusinessLogicException;
import com.gdsc.blended.common.image.exception.ExceptionCode;
import com.gdsc.blended.post.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;


    public ImageEntity createImage(String imagePath, PostEntity post) {
        ImageEntity entity = ImageEntity.builder()
                .post(post)
                .path(imagePath)
                .build();
        return imageRepository.save(entity);
    }

    public String findImageByPostId(Long postId) {
        ImageEntity entity = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
        return entity.getPath();
    }

    /*public void removeImage(Long imageId) {
        Image findPicture = findVerifiedImage(imageId);
        imageRepository.delete(findPicture);
    }*/

    private ImageEntity findVerifiedImage(Long imageId) {
        Optional<ImageEntity> findImage = imageRepository.findById(imageId);
        return findImage.orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
    }

    private ImageEntity findVerifiedPath(String path) {
        Optional<ImageEntity> findImage = imageRepository.findByPath(path);
        return findImage.orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
    }
}
