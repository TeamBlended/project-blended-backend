package com.gdsc.blended.alcohol.controller;


import com.gdsc.blended.alcohol.dto.AlcoholDto;
import com.gdsc.blended.alcohol.service.AlcoholService;
import com.gdsc.blended.common.image.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.apache.juli.logging.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alcohols")
public class AlcoholController {
    private final AlcoholService alcoholService;
    private final S3UploadService s3UploadService;


    @GetMapping("/search")
    public ResponseEntity<List<AlcoholDto>> searchAlcohols(@RequestParam("keyword") String keyword){
        List<AlcoholDto> alcoholDtoList = alcoholService.searchAlcohols(keyword);
        return ResponseEntity.ok(alcoholDtoList);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAlcohols(@RequestParam("file") MultipartFile multipartFile){
        String s3Path = "alcohol";
        String imageUrl = null;

        String alcoholName = Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);

        try {
            imageUrl = s3UploadService.upload(multipartFile, s3Path);
            alcoholService.uploadAlcoholsUrlInCsv(s3Path,alcoholName,imageUrl);
        } catch (IOException e) {
            throw new IllegalArgumentException("사진 S3 업로드 실패");
        }
        return "Hello World!";
    }

}

/*
    @PostMapping("/upload")
    public List<String> getAlcohols(@RequestParam("files") MultipartFile[] multipartFiles) throws IOException {
        String s3Path = "/alcohol";
        List<String> imageUrls = s3UploadService.uploadMulti(multipartFiles, s3Path);
        return imageUrls;
    }
    */