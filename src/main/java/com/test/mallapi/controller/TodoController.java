package com.test.mallapi.controller;

import com.test.mallapi.dto.PageRequestDTO;
import com.test.mallapi.dto.PageResponseDTO;
import com.test.mallapi.dto.TodoDTO;
import com.test.mallapi.service.TodoService;
import jakarta.annotation.Resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")
    public TodoDTO get(@PathVariable(name ="tno") Long tno) {
        return todoService.get(tno);
    }


    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {

        return todoService.list(pageRequestDTO);
    }

    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO) {
        log.info("TodoDTO: " + todoDTO);
        Long tno = todoService.register(todoDTO);
        return Map.of("TNO", tno);
    }

    @PutMapping("/{tno}")
    public Map<String, String> modify(
            @PathVariable(name = "tno") Long tno,
            @RequestBody TodoDTO todoDTO) {
        todoDTO.setTno(tno);
        log.info("Modify: " + todoDTO);

        todoService.modify(todoDTO);
        return Map.of("RESULT", "SUCCESS");
    }

    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable(name = "tno") Long tno) {
        log.info("Remove: " + tno);
        todoService.remove(tno);

        return Map.of("RESULT", "SUCCESS");
        /*
        DELETE의 경우에는 POST/PUT과 달리 전달하는 payload가 제한적이다. URL에 사용되는 특문을 사용하기 위해서는 URL인코딩 처리를 해줘야 한다.
        공백문자나 특문이 포함된 데이터는 정상적으로 처리되지 않기 때문에 주의 필요
        */
    }





}
