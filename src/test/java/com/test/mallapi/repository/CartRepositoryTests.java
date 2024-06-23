package com.test.mallapi.repository;

import com.test.mallapi.domain.Cart;
import com.test.mallapi.domain.CartItem;
import com.test.mallapi.domain.Member;
import com.test.mallapi.domain.Product;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.Optional;

@SpringBootTest
@Log4j2
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    // commit은 테스트가 끝난 후에도 트랜잭션을 유지할 것인지 여부를 결정한다. 즉, 테스트가 끝나면 롤백할 것인지 여부를 결정한다.
    @Transactional
    @Commit
    @Test
    public void testInsertByProduct() {
        log.info("test1-----------------");

        // 사용자가 전송하는 정보
        String email = "user1@aaa.com";
        Long pno = 5L;
        int qty = 2;

        // 만일 기존에 사용자의 장바구니 아이템이 있었다면
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if(cartItem != null) {
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);

            return;
        }

        // 기존에 사용자의 장바구니 아이템이 없었다면

        // 사용자가 장바구니를 만든 적이 있는지 확인
        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;

        // 사용자의 장바구니가 존재하지 않으면 장바구니 생성
        if(result.isEmpty()) {
            log.info("MemberCart is no exist!!");
            // 멤버 객체 생성
            Member member = Member.builder().email(email).build();

            // 멤버 객체를 이용해서 장바구니 객체 생성
            Cart tempCart = Cart.builder().owner(member).build();

            // 장바구니 저장
            cart = cartRepository.save(tempCart);
        } else {
            // 사용자의 장바구니가 존재하면 해당 장바구니를 가져온다.
            cart = result.get();
        }
        log.info("cart: " + cart);

        // --------------------------------------------

        // 장바구니에 상품이 있는지 확인. 없으면 상품 추가
        if(cartItem == null) {
            // 상품 객체 생성
            Product product = Product.builder().pno(pno).build();
            // 장바구니 아이템 객체 생성
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
        }

        // 상품 아이템 저장
        cartItemRepository.save(cartItem);

    }


}
