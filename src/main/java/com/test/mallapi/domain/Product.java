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
        imageList.add(productImage);
    }

    public void addImageString(String fileName){
        ProductImage productImage = ProductImage.builder().fileName(fileName).build();
        addImage(productImage);
    }

    public void clearImage(){
        imageList.clear();
    }


}
