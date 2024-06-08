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

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

                String contentType = multipartFile.getContentType();
                if(contentType != null && contentType.startsWith("image")) {
                    //  이미지 여부 확인
                    Path thumbnailPath = Paths.get(uploadPath, "s_" + saveName);

                    //  이미지 파일의 경우 썸네일을 생성한다. 썸네일 파일은 200x200 크기로 생성한다. 이로써 원본 파일이 생성될 때 s_로 시작하는 썸네일 파일도 같이 생성된다.
                    Thumbnails.of(savePath.toFile()).size(200, 200).toFile(thumbnailPath.toFile());
                }
                uploadNames.add(saveName);

            }catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } // end for
        return uploadNames;
    }

    /*
    getFile()은 ProductController에서 특정 파일을 조회할 때 사용된다.
    파일 데이터를 읽어서 ResponseEntity<Resource>로 반환한다. */
    public ResponseEntity<Resource> getFile(String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        if(!resource. isReadable()) {
            resource  = new FileSystemResource(uploadPath + File.separator + "default.jpeg");
        }

        HttpHeaders headers = new HttpHeaders();

        try {
            // getFile()은 파일 종류마다 다른 HTTP헤더 Content-Type을 생성해야 하기 때문에 Files.probeContentType()을 이용해서 헤더 메시지를 생성한다.
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
