package com.test.mallapi.repository;

import com.test.mallapi.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// CartRepository는 Cart 엔티티를 다루는 레포지토리이다. Cart 엔티티의 기본키 타입은 Long이다.
public interface CartRepository extends JpaRepository<Cart, Long> {

    // 이메일을 이용해서 회원의 장바구니 정보를 조회한다.
    @Query(" select cart from Cart cart where cart.owner.email = :email")
    public Optional<Cart> getCartOfMember(@Param("email") String email);


}
