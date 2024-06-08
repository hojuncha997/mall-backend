package com.test.mallapi.repository;


import com.test.mallapi.domain.Product;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void testInsert() {

        for(int i= 0; i < 10; i++) {

                Product product = Product.builder()
                        .pname("상품" + i)
                        .price(100 * i)
                        .pdesc("상품 설명" + i)
                        .build();

                // 2개의 상품 이미지 파일 추가
                product.addImageString(UUID.randomUUID().toString() + "_"+"IMAGE1.jpg");
                product.addImageString(UUID.randomUUID().toString() + "_"+"IMAGE2.jpg");

                productRepository.save(product);

                log.info("-----------------------------");
        }
    }



    /*
     * 엔티티로는 Product라는 하나의 엔티티 객체이지만 테이블에서는 2개의 테이블로 구성되기 때문에 JPA에서 이를 처리할 때
     * 한 번에 모든 테이블을 같이 로딩해서 처리할 것인지(eager loading) 아니면 필요한 테이블만 먼저 조회할 것인지(lazy loading)를 결정해야 한다.
     *
     * Product 엔티티 구성에 사용한 @ElementCollection은 기본적으로 lazy loading으로 동작하기 때문에 우선은 tbl_product에만 접근해서 데이터를 처리한다.
     * 첨부 파일이 필요하다면 product_image_list 테이블을 접근하게 된다. 이처럼 DB에 두 번 접근해서 처리해야 하므로 테스트 코드에는 @Transactional을 적용해야 한다.
     * */
    @Transactional
    @Test
    public void testRead() {
        Long pno = 1L;

        Optional<Product> result = productRepository.findById(pno);
        Product product = result.orElseThrow();

        log.info(product); // ------------- 1
        log.info(product.getImageList()); // ------------- 2
    }

    /*
    * toRead()의 결과는 1에서는 상품의 테이블만을 접근하지만 2를 실행하기 위해서는 상품 이미지 테이블에 접근하는 두 번의 쿼리가 실행된다.
    * 현재 Product 엔티티 클래스에는 @ToString(exclude = "imageList")가 적용되어 있어서 이미지 리스트를 제외하고 출력하도록 설정되어 있다.
    * 해당 설정이 없다면 이미지 리스트를 출력하기 위해서는 이미지 리스트 테이블에 접근하는 쿼리가 추가로 실행된다.
    * */

    @Test
    public void testRead2() {
        Long pno = 1L;

        Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();

        log.info(product);
        log.info(product.getImageList());

        /*
        *   testRead2()는 @Transactional 없이도 테이블들을 조인해서 한 번에 로딩한다.
        *  이는 Product 엔티티 클래스에 @EntityGraph(attributePaths = "imageList")를 적용했기 때문이다.
        **/
    }

    @Commit
    @Transactional
    @Test
    public void testDelete() {
        Long pno = 2L;

        productRepository.updateToDelete(pno, true);
    }

    @Test
    public void testUpdate() {
        Long pno = 10L;

        Product product = productRepository.selectOne(pno).get();

        product.changeName("10번 상품");
        product.changeDesc("10번 상품 설명입니다");
        product.changePrice(5000);

        // 첨부파일 수정
        product.clearList();

        product.addImageString(UUID.randomUUID().toString() + "_" + "NEWIMAGE1.jpg");
        product.addImageString(UUID.randomUUID().toString() + "_" + "NEWIMAGE2.jpg");
        product.addImageString(UUID.randomUUID().toString() + "_" + "NEWIMAGE3.jpg");

        productRepository.save(product);
    }

    @Test
    public void testList() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        //java.util
        result.getContent().forEach(arr -> {
           log.info(Arrays.toString(arr));
        });
    }

}

