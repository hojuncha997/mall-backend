package com.test.mallapi.config;

import com.test.mallapi.controller.formatter.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
    // 일시 문자열 데이터를 LocalDate로 변환 또는 그 반대의 역할을 하는 포매터를 등록
        registry.addFormatter(new LocalDateFormatter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(300)
                .allowedHeaders("Authorization", "Cache-Control" ,"Content-Type");
        /*
        * CORS 설정은 @Controller가 있는 클래스에 @CrossOrigin을 적용하거나 Spring Security를 이용하는 설정이 있다.
        * @CrossOrigin 설정은 모든 컨트롤러에 개별적으로 적용해야 하므로 여기에서는 WebMvcConfigurer의 설정으로 사용한다.
        * */
    }

}
