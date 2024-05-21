package com.test.mallapi.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    //  페이지 번호나 사이즈 등을 처리하기 위해 사용
    
    
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
}
