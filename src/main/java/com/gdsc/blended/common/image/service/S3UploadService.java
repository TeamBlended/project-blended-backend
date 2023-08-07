package com.gdsc.blended.common.image.service;


import com.gdsc.blended.common.apiResponse.PostResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@Service
public class S3UploadService {
    private static final int CAPACITY_LIMIT_BYTE = 1024 * 1024 * 10;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;

    @Autowired
    public S3UploadService(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public String upload(MultipartFile multipartFile, String filePath) throws IOException {
        // 파일 이름이 중복되지 않게 하기 위해 UUID 로 랜덤 값으로 파일 이름 생성
        String originName = multipartFile.getOriginalFilename();
        //확장자 추출
        String ext = originName.substring(originName.lastIndexOf(".") + 1);
        //중복 방지를 위해 파일명에 UUID 추가
        String s3FileName = UUID.randomUUID() + "." + ext;
        //키 생성
        String key = filePath + "/" + s3FileName;

        // 파일의 크기가 용량제한을 넘을 시 예외를 던진다.
        if (multipartFile.getSize() > CAPACITY_LIMIT_BYTE) {
            throw new ApiException(PostResponseMessage.IMAGE_TOO_LARGE);
        }

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(multipartFile.getContentType())
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

        GetUrlRequest urlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return s3Client.utilities().getUrl(urlRequest).toExternalForm();
    }

    public void delete(String path) {
        String bucketName = "blended-post"; // S3 버킷 이름
        String key = path.substring(path.lastIndexOf("com/") + 4); //S3 객체 키
        try {
            // S3 객체 삭제
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            System.out.println("S3 이미지 삭제 성공");
        } catch (S3Exception e) {
            System.err.println("S3 이미지 삭제 실패: " + e.awsErrorDetails().errorMessage());
        }

    }

    public String getImgUrl(String bucketName, String folderName, String imageName) {
        String key = folderName + "/" + imageName;
        GetUrlRequest urlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        return s3Client.utilities().getUrl(urlRequest).toExternalForm();
    }
}
