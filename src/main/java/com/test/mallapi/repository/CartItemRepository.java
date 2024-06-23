package com.test.mallapi.repository;

import com.test.mallapi.domain.CartItem;
import com.test.mallapi.dto.CartItemListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    /*
    * 특정한 사용자의 이메일을 통해서 해당 사용자의 모든 장바구니 아이템을 조회하는 기능
    *   - 로그인 했을 때 사용자가 담은 모든 장바구니 아이템 조회 시에 사용
    */
    @Query("select" +
    " new com.test.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName ) " +
            " from CartItem ci inner join Cart mc on ci.cart = mc " +
            " left join  Product p on ci.product = p " +
            " left join p.imageList pi" +
            " where mc.owner.email = :email and pi.ord = 0 " +
            " order by ci.cino desc" )
    public List<CartItemListDTO> getItemsOfCartDTOByEmail(@Param("email") String email);

    /*
     * 사용자의 이메일과 상품번호로 해당 장바구니 아이템을 알아내는 기능
     *   - 새로운 상품을 장바구니에 담고자 할 때 기존 장바구니 아이템인지 확인하기 위해서 필요
     */
    @Query("select ci from CartItem ci inner join Cart c on ci.cart = c where c.owner.email = :email and ci.product.pno = :pno")
    public CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);


    /*
     * 장바구니 아이템이 속한 장바구니의 번호를 알아내는 기능
     *   - 해당 아이템을 삭제한 후 해당 아이템이 속해 있는 장바구니의 모든 아이템을 알아내기 위해서 필요
     */
    @Query("select c.cno from Cart c inner join CartItem ci on ci.cart = c where ci.cino = :cino")
    public Long getCartFromItem(@Param("cino") Long cino);

    /*
     * 특정한 장바구니의 번호만으로 해당 장바구니의 모든 장바구니 아이템을 조회하는 기능
     *   - 특정한 장바구니 아이템을 삭제한 후에 해당 장바구니 아이템이 속해 있는 장바구니의 모든 장바구니 아이템을 조회할 때 필요
     */
    @Query("select new com.test.mallapi.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.pname, p.price, pi.fileName) " +
            " from CartItem ci inner join Cart mc on ci.cart = mc " +
            " left join Product p on ci.product = p " +
            " left join p.imageList pi " +
            " where mc.cno = :cno and pi.ord = 0 " +
            " order by ci.cino desc")
    public List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);

}

