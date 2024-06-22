package com.test.mallapi.service;


import com.test.mallapi.domain.Member;
import com.test.mallapi.domain.MemberRole;
import com.test.mallapi.dto.MemberDTO;
import com.test.mallapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    // MemberRepository 와 PasswordEncoder를 주입받아 이메일로 회원을 조회하거나 추가한다.
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDTO getKakaoMember(String accessToken) {

        // 현재는 카카오 사용자 이메일을 가져오려면 카카오에 비즈니스 심사를 요청해야 한다.
        // 따라서 대신 id를 가져온다.


        //  String email = getEmailFromKakaoAccessToken(accessToken);
        String id = getIdFromKakaoAccessToken(accessToken);
//        log.info("email: " + email);
        log.info("id: " + id);

//        Optional<Member> result = memberRepository.findById(email);
        Optional<Member> result = memberRepository.findById(id);
        // 기존 회원인 경우
        if(result.isPresent()) {
            return entityToDTO(result.get());
        }

        // 기존 회원이 아닌 경우
        // 닉네임은 소셜회원으로 설정하고, 패스워드는 임의로 생성한다.
//        Member socialMember = makeSocialMember(email);
        Member socialMember = makeSocialMember(id);
        memberRepository.save(socialMember);

        MemberDTO memberDTO = entityToDTO(socialMember);
        return memberDTO;

    }


    private String getIdFromKakaoAccessToken(String accessToken) {

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if(accessToken  == null) {
            throw new RuntimeException("Access Token is null");
        }

        // RestTemplate 객체 생성: RestTemplate은 HTTP 통신을 담당하는 객체로, HTTP 요청을 보내고 응답을 받는다. 동기 방식이다.
        RestTemplate restTemplate = new RestTemplate();


        // HttpHeaders 객체 생성: HttpHeaders는 HTTP 요청과 응답 헤더를 캡슐화하는 클래스로, 헤더를 설정할 수 있다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpEntity 객체 생성: HttpEntity는 HTTP 요청과 응답을 캡슐화하는 클래스로, 요청 헤더와 바디를 설정할 수 있다.
        // new HttpEntity<>(headers)는 요청 헤더만 설정한 것이다.
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // UriComponentsBuilder를 사용하면 URL을 쉽게 생성할 수 있다.
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        // RestTemplate.exchange() 메소드를 사용하면 HTTP 요청을 보낼 수 있다. 요청 방식, 요청 헤더, 요청 바디, 응답 타입을 설정할 수 있다.
        // 여기서는 GET 방식으로 요청을 보내고, 응답을 LinkedHashMap 타입으로 받는다.
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                LinkedHashMap.class
        );

        log.info(response);


        LinkedHashMap<String, Object> bodyMap = response.getBody();
        log.info("----------------------------------------");
        log.info(bodyMap);

        // id를 Long 타입으로 가져온 후 문자열로 변환
        Long id = (Long) bodyMap.get("id");
        log.info("kakaoId: " + id);

        return id.toString();
    }

    /*
    // 카카오 API를 사용해서 accessToken을 이용해 email을 가져오는 메소드
    그런데 현재는 카카오 사용자 이메일을 가져오려면 카카오에 비즈니스 심사를 요청해야 한다. 따라서 대신 id를 가져온다.


    private String getEmailFromKakaoAccessToken(String accessToken) {

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        if(accessToken  == null) {
            throw new RuntimeException("Access Token is null");
        }

        // RestTemplate 객체 생성: RestTemplate은 HTTP 통신을 담당하는 객체로, HTTP 요청을 보내고 응답을 받는다. 동기 방식이다.
        RestTemplate restTemplate = new RestTemplate();


        // HttpHeaders 객체 생성: HttpHeaders는 HTTP 요청과 응답 헤더를 캡슐화하는 클래스로, 헤더를 설정할 수 있다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpEntity 객체 생성: HttpEntity는 HTTP 요청과 응답을 캡슐화하는 클래스로, 요청 헤더와 바디를 설정할 수 있다.
        // new HttpEntity<>(headers)는 요청 헤더만 설정한 것이다.
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // UriComponentsBuilder를 사용하면 URL을 쉽게 생성할 수 있다.
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        // RestTemplate.exchange() 메소드를 사용하면 HTTP 요청을 보낼 수 있다. 요청 방식, 요청 헤더, 요청 바디, 응답 타입을 설정할 수 있다.
        // 여기서는 GET 방식으로 요청을 보내고, 응답을 LinkedHashMap 타입으로 받는다.
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                LinkedHashMap.class
        );

        log.info(response);

        // 응답 바디를 LinkedHashMap 타입으로 받아서 bodyMap에 저장한다.
        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("----------------------------------------");
        log.info(bodyMap);

        // bodyMap에서 kakao_account를 가져와서 kakaoAccount에 저장한다.
        LinkedHashMap<String,String> kakaoAccount = bodyMap.get("kakao_account");

        log.info("kakaAccount: " + kakaoAccount);

        // kakaoAccount에서 email을 가져와서 반환한다.
        return kakaoAccount.get("email");
    }

    */






    // 아래 함수는 임의로 패스워드를 생성하는 함수이다. 기존 회원이 아닐 때 임의로 비밀번호를 생성하여 저장하기 위해 사용된다.
    private String makeTemplatePassword() {


        // 자바 버전 21부터 사용 가능한 StringBuffer 대신 StringBuilder를 사용할 수도 있다.
        StringBuffer buffer = new StringBuffer();

        for(int i = 0; i < 10; i++) {
            // 아래 로직의 목적은 65 ~ 90 사이의 랜덤한 숫자를 생성하여 대문자 알파벳으로 변환하는 것이다.
            buffer.append((char)((int)(Math.random() * 55) + 65));
        }
        return buffer.toString();
    }

    // 이메일이 존재하지 않는다면 새로운 객체를 생성해야 한다.
    private Member makeSocialMember(String email) {

        String tempPassword = makeTemplatePassword();

        log.info("tempPassword: " + tempPassword);

        String nickname = "소셜회원";

        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname(nickname)
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;
    }

}


// 비동기로 통신하려면 webClient 를 사용할 수도 있다.
// 위 로직을 webClient를 사용해서 바꾸면 아래와 같이 된다.
//
// WebClient webClient = WebClient.builder()
//         .baseUrl("https://kapi.kakao.com/v2/user/me")
//         .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//         .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
//         .build();
//
// webClient.get()
//         .retrieve()
//         .bodyToMono(LinkedHashMap.class)
//         .subscribe(bodyMap -> {
//             log.info(bodyMap);
//             LinkedHashMap<String, LinkedHashMap> kakaoAccount = (LinkedHashMap<String, LinkedHashMap>) bodyMap.get("kakao_account");
//             log.info(kakaoAccount);
//             String email = kakaoAccount.get("email");
//             log.info(email);
//         });
//
// 위 코드는 비동기로 동작한다. subscribe() 메소드를 호출하면 비동기로 동작한다.
// subscribe() 메소드는 Mono 타입을 반환하고, Mono 타입은 비동기로 동작한다.
// Mono 타입은 0 또는 1개의 데이터를 발행하고, 데이터를 발행하면 구독자에게 데이터를 전달한다.
// Mono 타입은 Flux 타입과 달리 0 또는 1개의 데이터만 발행한다.
// Flux 타입은 0개 이상의 데이터를 발행한다.
// Mono 타입은 Mono.just() 메소드로 생성할 수 있다.
// Mono.just() 메소드는 1개의 데이터를 발행한다.

// 참고로, WebClient는 Spring WebFlux 모듈에 포함되어 있다.
// Spring WebFlux는 Spring 5부터 추가된 모듈로, 비동기로 동작하는 웹 애플리케이션을 개발할 수 있다.
// Spring WebFlux는 Reactor 라이브러리를 사용한다.\
// Reactor는 리액티브 프로그래밍을 위한 라이브러리로, Mono와 Flux 타입을 제공한다.





/*
    카카오 로그인 시, 만약 해당 회원이 없다면 샐운 회원을 추가할 때 패스워드를 임의로 생성한다.
    이렇게 생성된 패스워드는 PasswordEncoder를 통해서 알아볼 수 없게 된다. 따라서 관리자와 사용자 모두
    패스워드를 알 수 없게 된다.

    (패스워드를 모르기 때문에 일반 로그인은 불가능하게 된
    다. 대신 카카오 로그인 후에는 회원정보를 수정할 수 있도록 구성하여
    사용자가 원하는 패스워드를 저장할 수 있도록 유도한다.

 */