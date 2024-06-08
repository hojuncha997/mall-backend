package com.test.mallapi.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

// 값 타입 객체: 상품 이미지 정보를 담는 객체
@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String fileName;

    // 각 이미지마다 번호를 지정하고 상품 목록을 출력할 때 ord 값이 0번인 이미지들만 화면에서 볼 수 있도록 하기 위해 ord 필드를 추가
    private int ord;

    public void setOrd(int ord) {
        this.ord = ord;
    }
}

/*
* 파일 업로드의 특징은 파일 자체가 부수적인 요소라는 점이다.
* 상품 등록의 경우 핵심은 상품 자체이고 파일들은 이를 설명하는 부수적인 데이터이다.
* 상품은 고유한 PK를 가지는 하나의 온전한 엔티티로 봐야 하고 파일들은 엔티티에 속해 있는 데이터로 봐야 한다.
*
* JPA에서는 "값 타입 객체"라는 표현을 사용하는데, 컬렉션으로 처리할 때는 @ElementCollection을 사용하고,
* 일반적인 엔티티로 처리할 때는 @Embeddable을 사용한다.
*
* "값 타입 객체"는 엔티티와 달리 PK가 없는 데이터이다. 이 프로젝트에서 하나의 상품 데이터는 여러 개의 상품 이미지를 가지도록 구성된다.
* */