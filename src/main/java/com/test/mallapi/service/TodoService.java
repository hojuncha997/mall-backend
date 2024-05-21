package com.test.mallapi.service;

import com.test.mallapi.dto.TodoDTO;

public interface TodoService {
    Long register(TodoDTO todo);

    TodoDTO get(Long tno);

    void modify(TodoDTO todoDTO);

    void remove(Long tno);
}
