package com.test.mallapi.service;


import com.test.mallapi.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;


@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    @Override
    public MemberDTO getKakaoMember(String accessToken) {

        String email = getEmailFromKakaoAccessToken(accessToken);

        log.info("email: " + email);

        return null;
    }

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
