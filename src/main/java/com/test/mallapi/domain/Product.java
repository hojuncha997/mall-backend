/*
*
* 상품을 관리하는 Product는 일반 엔티티와 유사하지만 ProductImage의 목록을 가지고,
* 이를 관리하는 기능이 존재하도록 작성한다.
*
* 문자열로 파일을 추가하거나 ProductImage타입으로 이미지를 추가할 수 있도록 구성한다.
* @Embeddable을 사용하는 경우 PK가 생성되지 않기 때문에 모든 작업은
* PK를 가지는 엔티티로 구성한다는 특징이 있다.
* */


package com.test.mallapi.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_product")
@Getter
@ToString(exclude = "imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;

    private int price;

    private String pdesc;

    private boolean delFlag;

    // 값 타입 객체로 처리
    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    public void changePrice(int price){
        this.price = price;
    }

    public void changeDesc(String desc){
        this.pdesc = desc;
    }

    public void changeName(String name){
        this.pname = name;
    }

    public void addImage(ProductImage productImage){
        // 순서를 정하고 리스트에 집어 넣음
        productImage.setOrd(this.imageList.size());

        imageList.add(productImage);
    }

    public void addImageString(String fileName){
        ProductImage productImage = ProductImage.builder().fileName(fileName).build();
        addImage(productImage);
    }

    public void clearList(){
        imageList.clear();
    }
}


/*
*
* 엔티티로는 Product라는 하나의 엔티티 객체이지만 테이블에서는 2개의 테이블로 구성되기 때문에 JPA에서 이를 처리할 때
* 한 번에 모든 테이블을 같이 로딩해서 처리할 것인지(eager loading) 아니면 필요한 테이블만 먼저 조회할 것인지(lazy loading)를 결정해야 한다.
*
* Product 엔티티 구성에 사용한 @ElementCollection은 기본적으로 lazy loading으로 동작하기 때문에 우선은 tbl_product에만 접근해서 데이터를 처리한다.
* 첨부 파일이 필요하다면 product_image_list 테이블을 접근하게 된다. 이처럼 DB에 두 번 접근해서 처리해야 하므로 테스트 코드에는 @Transactional을 적용해야 한다.
* */


/*
 * toRead()의 결과는 1에서는 상품의 테이블만을 접근하지만 2를 실행하기 위해서는 상품 이미지 테이블에 접근하는 두 번의 쿼리가 실행된다.
 * 현재 Product 엔티티 클래스에는 @ToString(exclude = "imageList")가 적용되어 있어서 이미지 리스트를 제외하고 출력하도록 설정되어 있다.
 * 해당 설정이 없다면 이미지 리스트를 출력하기 위해서는 이미지 리스트 테이블에 접근하는 쿼리가 추가로 실행된다.
 * */


/*
* 실제 DB에서, 상품 삭제는 구매 기록과 이어지기 때문에 주의해야 한다. 특정 상품이 DB에서 삭제되면 해당 상품 데이터를 사용한 모든 구매나 문의 등의
* 데이터들이 함께 삭제되어야 한다(PK와 FK의 관계). 실제 모든 데이터들이 삭제되어야 한다면 일이 복잡해진다.
* 이에 대한 대안으로 실제 물리적 삭제(delete)가 아닌 논리적 삭제(delFlag)를 사용하는 방법이 있다. Soft Delete라고도 불리우는 이 방법은
* delete 대신에 update를 사용해서 delFlag를 true로 변경하는 방법이다. 대부분의 DB에는 boolean 타입이 없기 때문에 0과 1로 처리한다.
* ProductRepository에는 @Query와 @Modifying을 이용해서 update, delete 등의 JPQL을 실행할 수 있다.
*
* */


/*
 * 상품의 수정 부분은 Product의 ChangeXXX() 메서드를 이용해서 처리한다.
 * 다만 상품 이미지는 clearList()를 이용해서 모두 삭제하고 다시 ProductImage를 추가하는 방식으로 구성한다.
 * */

/*
* 목록 화면은 상품당 하나의 이미지가 포함되는 형태로 처리되어야 하기 때문에 @Query로 조인 처리해서 구성해야 한다.
* 상품 하나당 여러 개의 이미지 파일이 존재할 수 있기 때문에 상품 이미지의 ord 값이 0인 상품의 대표 이미지들만 처리해서 출력하도록 구성한다.
* ProductRepository에는 selectList() 메서드를 추가해서 처리한다.
* */