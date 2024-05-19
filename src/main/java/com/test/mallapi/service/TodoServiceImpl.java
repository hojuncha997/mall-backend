package com.test.mallapi.service;

import com.test.mallapi.dto.TodoDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class TodoServiceImpl implements TodoService {
    @Override
    public Long register(TodoDto todo) {
        log.info("Register todo: " + todo);
        return null;
    }

}
