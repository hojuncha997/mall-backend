package com.test.mallapi.controller;

import com.test.mallapi.dto.CartItemDTO;
import com.test.mallapi.dto.CartItemListDTO;
import com.test.mallapi.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/cart")
public class CartController {
    private final CartService cartService;


    // 장바구니에 상품 추가 혹은 변경
    @PreAuthorize("#itemDTO.email == authentication.name")  // 로그인한 사용자와 itemDTO의 email이 일치하는지 확인
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart (@RequestBody CartItemDTO itemDTO) {
        log.info("itemDTO: " + itemDTO);

        // 수량이 0 이하이면 삭제: 장바구니에서 삭제
        if(itemDTO.getQty() <= 0) {
            return cartService.remove(itemDTO.getCino());
        }

        return cartService.addOrModify(itemDTO);
    }

    // 장바구니 목록 조회
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDTO> getCartItems(Principal principal) {    // Principal은 로그인한 사용자의 정보를 담고 있다. 주로 다운캐스팅 해서 사용하거나 getName() 메서드를 사용한다.
        String email = principal.getName();
        log.info("----------------------------------------------");
        log.info("email: " + email);

        return cartService.getCartItems(email);
    }

    // 장바구니 아이템 삭제
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {
        log.info("cart item no: " + cino);
        return cartService.remove(cino);
    }


}

/*

/api/member를 제외하면 모든 기능은 JWTCheckFilter를 거치기 때문에 CartController는 스프링 시큐리티 관련 기능을 사용하도록 구현한다.
 */


/*
 requiredArgsConstructor를 사용하면 final이 붙은 필드에 대한 생성자를 만들어준다. 여기서 required가 붙은 이유는 final이 붙은 필드가 있기 때문이다.
 final은 필드가 한 번 초기화되면 다시는 바뀌지 않는다는 의미이다. 이러한 필드에 대해서는 생성자를 통해 초기화를 해주어야 한다.
 왜냐하면 생성자를 통해 초기화를 하지 않으면 컴파일 에러가 발생하기 때문이다.
 컴파일 에러가 발생하는 이유는 final이 붙은 필드가 초기화되지 않았기 때문이다.
 따라서 final이 붙은 필드에 대해서는 생성자를 통해 초기화를 해주어야 한다.
 이러한 번거로움을 해결하기 위해 lombok에서는 @RequiredArgsConstructor를 제공한다.
 이를 만약 수동으로 해주려면 아래와 같이 코드를 작성해야 한다.
 수동으로 초기화 해주려면 아래와 같이 코드를 작성해야 한다.
 @RestController

 @RequiredArgsConstructor
 public class CartController {
     private final CartService cartService;

     public CartController(CartService cartService) {
         this.cartService = cartService;
     }
 }
 이렇게 코드를 작성하면 final이 붙은 필드에 대한 생성자가 만들어진다.
 final이 붙은 필드를 사용하는 이유는 불변성을 유지하기 위해서이다.
 불변성을 유지하면 코드가 간결해지고, 가독성이 좋아진다.
 또한, 불변성을 유지하면 멀티스레드 환경에서도 안전하다.
 불변성을 유지하면 객체지향 프로그래밍의 특징을 살릴 수 있다.
 불변성이란 객체가 한 번 생성되면 그 상태가 변하지 않는 것을 의미한다.
 불변성을 유지하면 객체가 변하는 것을 막을 수 있다.
 불변성을 유지하면 객체가 변하지 않기 때문에 객체를 공유해도 안전하다.
 불변성을 유지하면 객체가 변하지 않기 때문에 객체를 여러 스레드가 동시에 사용해도 안전하다.
 불변성을 유지하면 객체가 변하지 않기 때문에 객체를 키로 사용해도 안전하다.
 불변성을 유지하면 객체가 변하지 않기 때문에 객체를 해시맵의 키로 사용해도 안전하다.
 불변성을 유지하면 객체가 변하지 않기 때문에 객체를 스트림의 요소로 사용해도 안전하다.
 불변성을 유지하면 객체가 변하지 않기 때문에 객체를 병렬 스트림의 요소로 사용해도 안전하다.
 불변성을 유지하면 객체가 변하지 않기 때문에 객체를 Optional의 값으로 사용해도 안전하다.

*/