package com.test.mallapi.controller.formatter;

import org.springframework.format.Formatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/*
* LocalDateFormatter
* */
public class LocalDateFormatter  implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale)  {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"
        ));
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(object);
    }
}

/*

    전달 받은 데이터를 서버에서 처리할 때 날짜와 시간을 주의해야 한다.
    브라우저에서는 문자열로 전송하지만 서버에서는 LocalDate 또는 LocalDateTime으로 처리한다.
    그렇기 때문에 이를 변환해 주는 formatter를 추가해서 이 과정을 자동화 한다.

    작성을 완료하면 스프링 MVC의 동작 과정에서 사용될 수 있도록 설정을 추가해 주어야 한다.
    config 패키지에 CustomServiceConfig 클래스를 추가해서 등록해준다.

*/
