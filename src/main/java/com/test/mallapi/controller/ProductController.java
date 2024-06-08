package com.test.mallapi.controller;

/*
* ProductDTO의 files를 이용해서 전송되는 첨부파일들을 처리하고 저장된 파일들의 이름은 나중에 DB에 보관하는 방식으로 사용
* */


import com.test.mallapi.dto.ProductDTO;
import com.test.mallapi.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {
    
    private final CustomFileUtil fileUtil;
    
    
    @PostMapping("/")
    public Map<String, String> register(ProductDTO productDTO) {
        
        log.info("register: " + productDTO);

        List<MultipartFile> files = productDTO.getFiles();  // 실제 파일들을 가져와서 멀티파트 리스트에 할당

        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileNames(uploadFileNames);

        return Map.of("RESULT", "SUCCESS");

    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);
    }
}
