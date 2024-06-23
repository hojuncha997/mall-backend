package com.test.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "tbl_cart_item", indexes = {
        @Index(name = "idx_cartitem_cart", columnList = "cart_cno"),
        @Index(name = "idx_cartitem_pno_cart", columnList = "product_pno")
})
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cino;

    @ManyToOne
    @JoinColumn(name = "product_pno")
    private Product product;

    @ManyToOne
    @JoinColumn(name="cart_cno")
    private Cart cart;

    private int qty;

    public void changeQty(int qty) {
        this.qty = qty;
    }
}

/*
* CartItem의 경우 수정할 수 있는 것이 수량 정도이므로 changeQty() 메서드 정도만 작성한다.
* 장바구니 아이템은 cino라는 pk가 있긴 하지만 특정한 상품이 특정한 장바구니에 있는지 조회하는 기능이
* 필요할 수 있으므로 인덱스를 설정해 둔다.
* */

