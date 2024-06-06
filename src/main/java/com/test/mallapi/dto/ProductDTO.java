package com.test.mallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


/*
* ProductDTO: 상품 정보처리를 위한 DTO
* */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long pno;
    private String pname;
    private int price;
    private String pdesc;
    private boolean delFlag;


    // 새롭게 서버에 보내지는 실제 파일 데이터를 의미.
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();


    //  완료된 파일 이름만 문자열로 보관한 리스트. 문자열로 업로드 된 결과만을 가지고 있기 때문에 DB에 파일 이름들을 처리하는 용도로 사용.
    @Builder.Default
    private  List<String> uploadFileNames = new ArrayList<>();
}
