package com.test.mallapi.controller;

import com.test.mallapi.dto.MemberDTO;
import com.test.mallapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/list")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MemberDTO>> getMemberList(@PageableDefault(size = 10, sort = "email") Pageable pageable) {
        log.info("getMemberList...");
        Page<MemberDTO> memberList = memberService.getMemberList(pageable);
        return ResponseEntity.ok(memberList);
        
        
        /*
        
        이렇게 하면 /api/member/list 엔드포인트로 GET 요청을 보낼 때 페이지 정보를 쿼리 파라미터로 전달할 수 있다.

        - /api/member/list?page=0&size=20: 첫 번째 페이지, 페이지당 20개 항목
        - /api/member/list?page=1&sort=nickname,desc: 두 번째 페이지, 닉네임 내림차순 정렬
        
        이 API는 페이지네이션된 회원 목록을 반환하며, 각 회원의 정보는 MemberDTO 형식으로 제공된다
         */
    }
}