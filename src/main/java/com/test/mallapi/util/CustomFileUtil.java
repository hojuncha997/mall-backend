package com.test.mallapi.util;

/*
 * CustomFileUtil:
 * 은 파일 데이터의 입출력을 담당한다.
 * 프로그램이 시작되면 upload라는 이름의 폴더를 체크해서 자동으로 생성하도록
 * @PostConstruct를 이용하고
 * 파일 업로드 작업은 saveFiles()로 작성한다.
 * */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;




@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    @Value("${com.test.mallapi.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);

        if(tempFolder.exists() == false) {
            tempFolder.mkdirs();
        }

        uploadPath = tempFolder.getAbsolutePath();

        log.info("-----------------------------------------------------------");
        log.info(uploadPath);
    }

    /*
    * 파일 저장 시 중복된 이름의 파일이 저장되는 것을 막기 위해 UUID로 중복이 발생하지 않도록 파일명을 구성한다.
    * 파일명은 "UUID값_파일명"의 형태로 구성된다.
    *
    * 이 메서드는 ProductController에서 사용된다.
    * */
    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        if(files == null || files.size() == 0) {
            return List.of();
        }
        List<String> uploadNames = new ArrayList<>();

        for (MultipartFile multipartFile : files) {
            String saveName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, saveName);

            try {
                Files.copy(multipartFile.getInputStream(), savePath);
                uploadNames.add(saveName);

            }catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } // end for
        return uploadNames;
    }

}
