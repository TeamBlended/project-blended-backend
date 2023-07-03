package com.gdsc.blended.common.image.service;

import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.common.image.repository.ImageRepository;
import com.gdsc.blended.common.image.exception.BusinessLogicException;
import com.gdsc.blended.common.image.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private ImageRepository imageRepository;


    public ImageEntity createImage(ImageEntity image) {
        return imageRepository.save(image);
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
