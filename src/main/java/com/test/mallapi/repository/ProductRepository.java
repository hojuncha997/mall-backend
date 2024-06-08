package com.test.mallapi.repository;

import com.test.mallapi.domain.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


/*
 * 쿼리 실행횟수는 가능한 적으면 좋다.
 * JPA에서는 쿼리를 작성할 때 @EntityGraph를 이용해서 해당 속성을 조인 처리하도록 설정할 수 있다.
 * ProductRepository 내에 @Query로 메서드를 추가한다.
 * */

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = "imageList")  // 의미는 Product 엔티티를 조회하는데 imageList를 조인해서 가져오도록 설정
    @Query("select p from Product p where p.pno = :pno") // 의미는 Product 엔티티를 조회하는데 pno가 일치하는 것을 가져오도록 설정.
    Optional<Product> selectOne(@Param("pno") Long pno); // pno를 파라미터로 받아서 Product 엔티티를 조회하는 메서드. 만약 pno가 존재하지 않으면 Optional.empty()를 반환한다.


    @Modifying
    @Query("update Product p set p.delFlag = :flag where p.pno = :pno")  // 의미는 Product 엔티티를 수정하는데 pno가 일치하는 것을 업데이트 처리하도록 설정.
    void updateToDelete(@Param("pno") Long pno, @Param("flag") boolean flag); // pno를 파라미터로 받아서 Product 엔티티를 수정하는 메서드. delFlag를 true로 변경한다.




}

