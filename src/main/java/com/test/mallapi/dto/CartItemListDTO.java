package com.test.mallapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {

    private Long cino;

    private int qty;

    private Long pno;

    private String pname;

    private int price;

    private String imageFile;


    // 직접 생성자 정의: JPQL을 이용해서 직접 DTO 객체를 생성하는 Projection이라는 방식을 이용하기 위함이다.
    public CartItemListDTO(Long cino, int qty, Long pno, String pname, int price, String imageFile) {

        this.cino = cino;
        this.qty = qty;
        this.pno = pno;
        this.pname = pname;
        this.price = price;
        this.imageFile = imageFile;
    }
}


/*
* 컨트롤러로 전달되는 목록 데이터는 특정 사용자의 장바구니에 포함된 상품의 정보들과 수량, 이미지 파일들이다.
* 이를 CartItemListDTO 클래스를 정의한다.
* */