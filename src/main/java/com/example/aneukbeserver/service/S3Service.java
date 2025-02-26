package com.example.aneukbeserver.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.aneukbeserver.domain.diary.Diary;
import com.example.aneukbeserver.domain.member.Member;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile -> File & upload S3
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        log.info(String.valueOf(multipartFile));
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> 전환 실패"));

        return S3UploadUrl(uploadFile, dirName);
    }

    private String S3UploadUrl(File uploadFile, String dirName) {
        String uploadImageUrl = putS3(uploadFile, dirName);

        removeNewFile(uploadFile); // convert()함수로 인해 로컬에 생성된 파일 삭제

        return uploadImageUrl;
    }

    private void removeNewFile(File uploadFile) {
        if (uploadFile.delete())
            log.info("파일이 삭제되었습니다");
        else
            log.info("파일이 삭제되지 못했습니다.");
    }

    private String putS3(File uploadFile, String fileName) {
        log.info("Uploading file: {} to bucket: {} with key: {}", uploadFile.getAbsolutePath(), bucket, fileName);
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
        );
        log.info("Upload successful. Generating URL...");
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename()); // 업로드한 파일 이름
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


    public String getImage(Member member, Diary diary) {
        String key = member.getEmail() + "/" + diary.getId();
        URL url = amazonS3.getUrl(bucket,  key);
        String urlText = ""+ url;

        return urlText;
    }



}
