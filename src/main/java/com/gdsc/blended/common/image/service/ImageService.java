package com.gdsc.blended.common.image.service;

import com.gdsc.blended.common.image.entity.Image;
import com.gdsc.blended.common.image.repository.ImageRepository;
import com.gdsc.blended.common.image.exception.BusinessLogicException;
import com.gdsc.blended.common.image.exception.ExceptionCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class ImageService {
    private ImageRepository imageRepository;

    public ImageService(ImageRepository ImageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image createImage(Image image) {
        return imageRepository.save(image);
    }

    public void removeImage(Long imageId) {
        Image findPicture = findVerifiedImage(imageId);
        imageRepository.delete(findPicture);
    }

    private Image findVerifiedImage(Long imageId) {
        Optional<Image> findImage = imageRepository.findById(imageId);
        return findImage.orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
    }

    private Image findVerifiedPath(String path) {
        Optional<Image> findImage = imageRepository.findByPath(path);
        return findImage.orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
    }
}
