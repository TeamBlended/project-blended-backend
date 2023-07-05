package com.gdsc.blended.common.image.service;


import com.gdsc.blended.common.image.repository.ImageRepository;

import lombok.RequiredArgsConstructor;
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
import java.net.URLDecoder;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3UploadService {
    private final ImageRepository imageRepository;
    private static final int CAPACITY_LIMIT_BYTE = 1024 * 1024 * 10;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.dir}")
    private String filePath;

    //private final AmazonS3 amazonS3;
    private final S3Client s3Client;

    public String upload(MultipartFile multipartFile) throws IOException{
        // 파일 이름이 중복되지 않게 하기 위해 UUID 로 랜덤값 생성하여 "-"로 파일 이름과 연결하여 파일 이름 생성
        String s3FileName = filePath + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        // 파일의 크기가 용량제한을 넘을 시 예외를 던진다.
        if (multipartFile.getSize() > CAPACITY_LIMIT_BYTE) {
            throw new RuntimeException("이미지가 10M 제한을 넘어갑니다.");
        }

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(filePath + s3FileName)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

        GetUrlRequest urlRequest = GetUrlRequest.builder()
                .bucket(bucket)
                .key(s3FileName)
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
}

/*//jwt
    public void delete(String path) {
        DeleteObjectRequest request = getDeleteObjectRequest(path);
        s3Client.deleteObject(request);
//      String key = URLDecoder.decode(path.replace("s3://blended-post/post/", ""));
//      amazonS3.deleteObject(bucket, key);
    }

    private DeleteObjectRequest getDeleteObjectRequest(String path) {
        return DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(path.substring(path.lastIndexOf("com/") + 4))
                .build();
    }*/


/*public String upload(MultipartFile multipartFile) throws IOException{
        // 파일 이름이 중복되지 않게 하기 위해 UUID 로 랜덤값 생성하여 "-"로 파일 이름과 연결하여 파일 이름 생성
        String s3FileName = filePath + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        // 파일의 크기가 용량제한을 넘을 시 예외를 던진다.
        if (multipartFile.getSize() > CAPACITY_LIMIT_BYTE) {
            throw new RuntimeException("이미지가 10M 제한을 넘어갑니다.");
        }
        // S3에 알려줄 파일 메타 데이터 정보에 파일 크기를 담는다.
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        s3Client.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        return s3Client.getUrl(bucket, s3FileName).toString();
    }*/

//delete
    /*public void delete(String path) {
        String key = extractKeyFromPath(path);
        DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucket, key);
        s3Client.deleteObject(deleteRequest);
    }

    private String extractKeyFromPath(String path) {
        if (path.startsWith("https://")) {
            int startIndex = path.indexOf("com/*", 5);
            if (startIndex != -1) {
                return path.substring(startIndex + 1);
            }
        }
        throw new IllegalArgumentException("Invalid S3 path: " + path);
    }*/