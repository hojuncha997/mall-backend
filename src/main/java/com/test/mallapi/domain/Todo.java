package com.test.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

//엔티티를 사용해서 DB와 애플리케이션 사이의 데이터를 동기화 하고 관리
@Entity
@Table(name="tbl_todo")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {


    @Id // DB의 pk가 됨
    // 고유한 pk를 가지게 하기 위해서 자동생성 방식을 사용. 이는 PK를 DB에서 자동 생성한다는 의미임.( 마리아디비의 경우 auto_increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    private String title;
    private String writer;
    private boolean complete;
    private LocalDate dueDate;
    
    //    아래 함수 추가

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeComplete(boolean complete) {
        this.complete = complete;
    }

    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}