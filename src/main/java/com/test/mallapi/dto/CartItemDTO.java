package com.test.mallapi.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    
    private String email;
    private Long pno;
    private int qty;
    private Long cino;

}


/*
* CartItemDTO는 아래와 같은 상황에서 사용된다.
* 
* 상품을 조회하는 화면에서 사용자가 자신의 장바구니에 상품을 추가하는 경우
*   - 전달되는 데이터는 이메일, 상품번호, 수량이다.
* 
* 장바구니 아이템 목록에서 상품 수량을 조정하는 경우
*   - 이미 만들어진 장바구니 아이템 번호(cino)와 변경하고자 하는 수량
* 
* 
* */