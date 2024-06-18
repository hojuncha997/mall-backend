/*
    스프링 시큐리티는 사용자 인증 처리를 위해서 UserDetailsService라는 인터페이스 구현체를 사용한다.
    이를 커스터마이징 하기 위해 프로젝트 내에 security 패키지를 만들고 CustomuserDetailsService를 추가하였다.

    시큐리티를 적용하면 CustomUserDetailsService의 loadByUsername()에서 사용자 정보를 조회하고,
    해당 사용자의 인증과 권한을 처리하게 된다.

 */

package com.test.mallapi.security;

import com.test.mallapi.domain.Member;
import com.test.mallapi.dto.MemberDTO;
import com.test.mallapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("---------------loadUserByUsername--------------");

        Member member = memberRepository.getWithRoles(username);

        if(member == null) {
            throw new UsernameNotFoundException("Not Found");
        }

        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList()
                        .stream()
                        .map(memberRole -> memberRole.name()).collect(Collectors.toList())
        );

        log.info(memberDTO);
        return memberDTO;
    }
}