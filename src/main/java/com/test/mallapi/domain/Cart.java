package com.test.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "owner")
@Table(name = "tbl_cart", indexes = {@Index(name="idx_cart_email", columnList = "member_owner")})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    @OneToOne
    @JoinColumn(name = "member_owner")
    private Member owner;
}


/*
*
* Cart 엔티티 클래스는 회원과 일대일 관계를 맺는다.
* tbl_cart라는 이름의 테이블을 생성한다. 주로 사용자의 이메일을 통해 검색하게 되므로 @Index를 이용해서 테이블 내에
* 인덱스를 생성한다.
*
* 장바구니 아이템은 상품, 수량을 속성으로 가지고 Cart와는 다대일(ManyToOne)의 관계로 tbl_cart_item 이름의 테이블로 생성한다.
*
* */