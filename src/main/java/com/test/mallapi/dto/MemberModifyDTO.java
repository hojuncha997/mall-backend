package com.test.mallapi.dto;

import lombok.Data;

@Data
public class MemberModifyDTO {

    private String email;
    private String pw;
    private String nickname;
}

/*
* MemberDTO는 스프링 시큐리티와 관련해서 생성자가 존재하므로
* 컨트롤러에서 파라미터 수집 시에 불편하다. 따라서 이 DTO를 생성했다.
* */
