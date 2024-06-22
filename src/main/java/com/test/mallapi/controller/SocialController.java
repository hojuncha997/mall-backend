package com.test.mallapi.controller;


import com.test.mallapi.dto.MemberDTO;
import com.test.mallapi.service.MemberService;
import com.test.mallapi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public Map<String, Object> getMemberFromKakao(String accessToken) {
        log.info("accessToken: ");
        log.info(accessToken);

        // 기존 회원 여부를 확인하고, 없으면 추가한다. 이 때 비밀번호는 임의, 닉네임은 소셜회원으로 설정한다.
        MemberDTO memberDTO = memberService.getKakaoMember(accessToken);

        // 회원정보를 반환한다. 여기서 claims란 회원정보를 의미한다. 이 정보들을 가지고 Map을 만들어 반환한다. Map이란 key-value 쌍으로 이루어진 자료구조이다.
        Map<String, Object> claims = memberDTO.getClaims();

        // JWT 토큰과 refresh을 생성한다. claims와 만료시간을 인자로 받는다.
        String jwtAccessToken = JWTUtil.generateToken(claims, 10);  // 10분
        String jwtRefreshToken = JWTUtil.generateToken(claims, 60*24);  // 24시간

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        return claims;

        // Map을 리턴한다는 것은 쉽게 말하면 JSON 형태로 데이터를 반환한다는 것이다. 이 데이터는 클라이언트에게 전달된다.
    }

}
