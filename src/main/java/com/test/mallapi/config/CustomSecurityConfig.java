package com.test.mallapi.config;


import com.test.mallapi.security.filter.JWTCheckFilter;
import com.test.mallapi.security.handler.APILoginFailHandler;
import com.test.mallapi.security.handler.APILoginSuccessHandler;
import com.test.mallapi.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("-------------------------Security Config-------------------------");

        //  cors 설정
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        //  서버에서 내부 세션을 생성하지 않도록  설정
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //  csrf사용 X
        http.csrf(config -> config.disable());

        //  API서버로 로그인할 수 있도록 설정
        http.formLogin(config -> {
            //  이 설정을 추가하면 POST 방식으로 username과 password란느 파라미터를 통해 로그인 처리 가능
            config.loginPage("/api/member/login");
            //  로그인 후 처리
            config.successHandler(new APILoginSuccessHandler());
            //  로그인 실패 후 처리
            config.failureHandler(new APILoginFailHandler());
        });
        
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class); //  JWT 체크

        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new CustomAccessDeniedHandler());
        });

        return http.build();
    }

    //  이 CORS설정이 파라미터로 들어감
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration  = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    /* 스프링 시큐리티는 사용자 패스워드에 PasswordEncoder를 설정해 줘야 한다.*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }





}


/*
* 스프링 시큐리티와 관련된 설정을 위한 파일
*
* */