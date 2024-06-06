package com.test.mallapi.config;

import com.test.mallapi.controller.formatter.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
//        일시 문자열 데이터를 LocalDate로 변환 또는 그 반대의 역할을 하는 포매터를 등록
        registry.addFormatter(new LocalDateFormatter());
    }
}
