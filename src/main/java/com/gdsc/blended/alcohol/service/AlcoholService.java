package com.gdsc.blended.alcohol.service;

import com.gdsc.blended.alcohol.dto.AlcoholCameraResponseDto;
import com.gdsc.blended.alcohol.dto.AlcoholDto;
import com.gdsc.blended.alcohol.entity.AlcoholEntity;
import com.gdsc.blended.alcohol.repository.AlcoholRepository;
import com.gdsc.blended.common.message.AlcoholResponseMessage;
import com.gdsc.blended.common.message.ApiResponse;
import com.gdsc.blended.common.message.PostResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
import com.gdsc.blended.common.image.entity.ImageEntity;
import com.gdsc.blended.common.image.repository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class AlcoholService {
    private final AlcoholRepository alcoholRepository;
    private final ImageRepository imageRepository;


    @Transactional
    public List<AlcoholDto> searchAlcohols(String keyword) {
        List<AlcoholEntity> findAlcohols = alcoholRepository.findByWhiskyKoreanContainingOrWhiskyEnglishContaining(keyword, keyword);
        List<AlcoholDto> alcoholDtoList = new ArrayList<>();

        if (findAlcohols.isEmpty()) {
            throw new ApiException(AlcoholResponseMessage.ALCOHOL_NOT_FOUND);
        }

        for (AlcoholEntity alcoholEntity : findAlcohols) {
            alcoholDtoList.add(this.convertEntityToDto(alcoholEntity));
        }
        return alcoholDtoList;
    }

    @Transactional
    public void uploadAlcoholsUrlInCsv(String folderName,String alcoholName ,String imageURL) throws IOException {
        String csvFilePath = "src/main/resources/위스키 타입_329개.csv";
        System.out.println(alcoholName);
        String realAlcoholName = alcoholName.split("\\.")[0];
        try {
            // Read the CSV file
            Path path = Path.of(csvFilePath);
            List<String> lines = Files.readAllLines(path);

            String header = lines.get(0);

            List<String> modifiedLines = new ArrayList<>(lines);
            modifiedLines.set(0, header);
            for (int i = 1; i < modifiedLines.size(); i++) {
                String line = modifiedLines.get(i);
                String[] columns = line.split(",");
                String bucketName = "blended-post";
                String FolderName = folderName + "/";
                String imageName = columns[0]; // 가져올 이미지 파일 이름

                if (realAlcoholName.equals(imageName)) {
                    line += imageURL;
                    modifiedLines.set(i, line);
                    //db에 저장
                    AlcoholEntity alcohol = AlcoholEntity.builder()
                            .whiskyKorean(columns[0])
                            .whiskyEnglish(columns[1])
                            .abv(columns[2])
                            .country(columns[3])
                            .type(columns[4])
                            .imgUrl(imageURL)  // Assuming the alcohol_image_url column is at index 5
                            .build();
                    alcoholRepository.save(alcohol);
                    break;
                }
                else {
                    log.info("이미지가 존재하지 않습니다.");
                }
            }
            Files.write(path, modifiedLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
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