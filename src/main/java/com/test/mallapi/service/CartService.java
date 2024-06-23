package com.test.mallapi.service;

import com.test.mallapi.dto.CartItemDTO;
import com.test.mallapi.dto.CartItemListDTO;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface CartService {

    // 장바구니 아이템 추가 혹은 변경: 없으면 추가, 있으면 수량 변경
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

    // 모든 장바구니 아이템 목록 조회
    public List<CartItemListDTO> getCartItems(String email);

    // 장바구니 아이템 삭제
    public List<CartItemListDTO> remove(Long cino);
}


/*
* 메서드들의 리턴 타입이 모두 List<CartItemListDTO>인 것은 장바구니 아이템을 처리한 후에는
* 화면에 새로 갱신해야 하는 장바구니 아이템들의 데이터가 필요하기 때문이다.
*
*
* */