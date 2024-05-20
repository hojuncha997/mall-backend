package com.test.mallapi.service;

import com.test.mallapi.domain.Todo;
import com.test.mallapi.dto.TodoDto;

public interface TodoService {
    Long register(TodoDto todo);

    TodoDto get(Long tno);

    void modify(TodoDto todoDto);

    void remove(Long tno);
}
