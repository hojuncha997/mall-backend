package com.test.mallapi.service;

import com.test.mallapi.domain.Todo;
import com.test.mallapi.dto.TodoDto;
import com.test.mallapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor // 생성자 자동 주입
public class TodoServiceImpl implements TodoService {

    // 자동 주입 대상은 final로(@RequiredArgsConstructor가 final 또는 @NonNull로 선언된 필드에 대해 필요한 생성자를 자동으로 생성)
    private final ModelMapper modelMapper;
    private final TodoRepository todoRepository;


    @Override
    public Long register(TodoDto todoDto) { // 등록
        log.info(".........");

        Todo todo = modelMapper.map(todoDto, Todo.class);
        Todo savedTodo = todoRepository.save(todo);

        return savedTodo.getTno();
    }

    @Override
    public TodoDto get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);


        Todo todo = result.orElseThrow();
        TodoDto dto = modelMapper.map(todo, TodoDto.class);

        return dto;
    }

    @Override
    public void modify(TodoDto todoDto) {
        Optional<Todo> result = todoRepository.findById(todoDto.getTno());

        Todo todo = result.orElseThrow();

        todo.changeTitle(todoDto.getTitle());
        todo.changeDueDate(todoDto.getDueDate());
        todo.changeComplete(todoDto.isComplete());

        todoRepository.save(todo);
    }

    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }


}
