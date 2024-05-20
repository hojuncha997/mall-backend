package com.test.mallapi;

import com.test.mallapi.dto.TodoDto;
import com.test.mallapi.service.TodoService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Test
    public void testRegister() {
        TodoDto todoDto = TodoDto.builder()
                .title("서비스 테스트")
                .writer("tester")
                .dueDate(LocalDate.of(2024, 05, 19))
                .build();

        Long tno = todoService.register(todoDto);
        log.info("Tno: ", tno);

    }

    @Test
    public void testGet() {
        Long tno = 101L;
        TodoDto todoDto = todoService.get(tno);
        log.info(todoDto);
    }

    @Test
    public void testUpdate() {
        TodoDto todoDto = TodoDto.builder()
                .tno(101L)
                .title("서비스 수정")
                .writer("tester2")
                .dueDate(LocalDate.of(2025, 03, 11))
                .build();
        todoService.modify(todoDto);

    }

    @Test
    public void testDelete() {
        Long tno = 101L;
        todoService.remove(tno);
    }
}
