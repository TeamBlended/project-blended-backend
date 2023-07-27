package com.gdsc.blended.alcohol.controller;


import com.gdsc.blended.alcohol.dto.AlcoholDto;
import com.gdsc.blended.alcohol.service.AlcoholService;
import com.gdsc.blended.common.image.service.S3UploadService;
import com.gdsc.blended.common.apiResponse.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alcohols")
public class AlcoholController {
    private final AlcoholService alcoholService;
    private final S3UploadService s3UploadService;


    @Operation(summary = "개발자용")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AlcoholDto>>> searchAlcohols(@RequestParam("keyword") String keyword){

        List<AlcoholDto> alcoholDtoList = alcoholService.searchAlcohols(keyword);
        ApiResponse<List<AlcoholDto>> response = ApiResponse.success(alcoholDtoList);
        return ResponseEntity.ok(response);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadAlcohols(@RequestParam("file") MultipartFile multipartFile){
        String s3Path = "alcohol";
        String imageUrl = null;
        String alcoholName = Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);

        try {
            imageUrl = s3UploadService.upload(multipartFile, s3Path);
            alcoholService.uploadAlcoholsUrlInCsv(s3Path,alcoholName,imageUrl);
            ApiResponse<String> apiResponse = new ApiResponse<String>(imageUrl, HttpStatus.OK, "SUCCESS");
            return ResponseEntity.ok(apiResponse);
        } catch (IOException e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, HttpStatus.BAD_REQUEST, "사진 S3 업로드 실패");
            return ResponseEntity.status(500).body(apiResponse);
        }
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