package com.test.mallapi.service;

import com.test.mallapi.domain.Member;
import com.test.mallapi.dto.MemberDTO;
import com.test.mallapi.dto.MemberModifyDTO;
import jakarta.transaction.Transactional;

import java.util.stream.Collectors;


@Transactional
public interface MemberService {
    /*
    * API 서버가 AccessToken 을 처리해서 사용자의 이메일 정보를 추출하는 것을 확인했다면
    * 이를 사용해서 DB를 조회하여 기존 회원 여부를 확인해야 한다. 회원정보는 MmemberDTO 타입을 통해 처리돼야 하므로
    * Member엔티티 객체를 DTO로 변환하는 메소드를 추가한다.
    * */
    MemberDTO getKakaoMember(String accessToken);


    void modifyMember(MemberModifyDTO memberModifyDTO);


    default MemberDTO entityToDTO(Member member) {
        MemberDTO dto = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));

        return dto;

    }



}
