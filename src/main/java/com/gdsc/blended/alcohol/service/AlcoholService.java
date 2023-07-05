package com.gdsc.blended.alcohol.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.gdsc.blended.alcohol.dto.AlcoholDto;
import com.gdsc.blended.alcohol.entity.AlcoholEntity;
import com.gdsc.blended.alcohol.repository.AlcoholRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AlcoholService {
    private final AlcoholRepository alcoholRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String filePath;

    private final AmazonS3 amazonS3;

    @Transactional
    public List<AlcoholDto> searchAlcohols(String keyword) {
        List<AlcoholEntity> findAlcohols = alcoholRepository.findByWhiskyKoreanOrWhiskyEnglishContaining(keyword, keyword);
        List<AlcoholDto> alcoholDtoList = new ArrayList<>();

        if (findAlcohols.isEmpty()) return alcoholDtoList;

        for (AlcoholEntity alcoholEntity : findAlcohols) {
            alcoholDtoList.add(this.convertEntityToDto(alcoholEntity));
        }
        return alcoholDtoList;
    }

    @Transactional
    public void uploadAlcoholsUrlInCsv() {
        String filePath = "src/main/resources/위스키 타입_329개.csv";

        try {
            // Read the CSV file
            Path path = Path.of(filePath);
            List<String> lines = Files.readAllLines(path);

            String header = lines.get(0);

            List<String> modifiedLines = new ArrayList<>(lines);
            modifiedLines.set(0, header);
            for (int i = 1; i < modifiedLines.size(); i++) {
                String line = modifiedLines.get(i);
                String[] columns = line.split(",");
                String bucketName = "blended-post";
                String folderName = "alcohol/"+columns[0];
                String imageName = columns[0]; // 가져올 이미지 파일 이름
                String imageUrl = inssertExtantion(bucketName,folderName,imageName);

                /*
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, folderName + "/" + image);
                String imageUrl = amazonS3.generatePresignedUrl(urlRequest).toString();
*/
                line +=   imageUrl;
                modifiedLines.set(i, line);

                //db에 저장
                AlcoholEntity alcohol = AlcoholEntity.builder()
                        .whiskyKorean(columns[0])
                        .whiskyEnglish(columns[1])
                        .abv(columns[2])
                        .country(columns[3])
                        .type(columns[4])
                        .imgUrl(imageUrl)  // Assuming the alcohol_image_url column is at index 5
                        .build();


                alcoholRepository.save(alcohol);
            }
            Files.write(path, modifiedLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private  String inssertExtantion(String bucketName,String folderName, String imageName){
// 이미지 파일 확장자 확인
        List<S3ObjectSummary> objectSummaries = amazonS3.listObjects(bucketName, folderName).getObjectSummaries();
        String fileExtension = null;

        for (S3ObjectSummary objectSummary : objectSummaries) {
            if (objectSummary.getKey().startsWith(folderName + "/" + imageName)) {
                String key = objectSummary.getKey();
                fileExtension = key.substring(key.lastIndexOf(".") + 1).toLowerCase();
                break;
            }
        }
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, folderName + "/" + imageName + "." + fileExtension);
            String imageUrl = amazonS3.generatePresignedUrl(urlRequest).toString();
            return imageUrl;
    }

    private AlcoholDto convertEntityToDto(AlcoholEntity alcoholEntity) {
        return AlcoholDto.builder()
                .id(alcoholEntity.getId())
                .whiskyKorean(alcoholEntity.getWhiskyKorean())
                .whiskyEnglish(alcoholEntity.getWhiskyEnglish())
                .abv(alcoholEntity.getAbv())
                .country(alcoholEntity.getCountry())
                .type(alcoholEntity.getType())
                .imgUrl(alcoholEntity.getImgUrl())
                .build();
    }
}

