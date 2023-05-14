package com.gdsc.blended.common.image.controller;

import com.gdsc.blended.common.image.dto.ImageDto;
import com.gdsc.blended.common.image.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/images")
public class ImageUploadController {
    private final S3UploadService s3UploadService;

    //이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity uploadImage(MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(s3UploadService.upload(multipartFile), HttpStatus.CREATED);
    }

    /* 이미지 삭제
    @DeleteMapping("/delete")
    public ResponseEntity deleteFile(@RequestBody ImageDto imageDto) {
        s3UploadService.delete(imageDto.getImage());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/
}
