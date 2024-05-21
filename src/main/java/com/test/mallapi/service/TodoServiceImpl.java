package com.test.mallapi.service;

import com.test.mallapi.domain.Todo;
import com.test.mallapi.dto.PageRequestDTO;
import com.test.mallapi.dto.PageResponseDTO;
import com.test.mallapi.dto.TodoDTO;
import com.test.mallapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor // 생성자 자동 주입
public class TodoServiceImpl implements TodoService {

    // 자동 주입 대상은 final로(@RequiredArgsConstructor가 final 또는 @NonNull로 선언된 필드에 대해 필요한 생성자를 자동으로 생성)
    private final ModelMapper modelMapper;
    private final TodoRepository todoRepository;


    @Override
    public Long register(TodoDTO todoDTO) { // 등록
        log.info(".........");

        Todo todo = modelMapper.map(todoDTO, Todo.class);
        Todo savedTodo = todoRepository.save(todo);

        return savedTodo.getTno();
    }

    @Override
    public TodoDTO get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);


        Todo todo = result.orElseThrow();
        TodoDTO dto = modelMapper.map(todo, TodoDTO.class);

        return dto;
    }

    @Override
    public void modify(TodoDTO todoDto) {
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


    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()  - 1, // 1페이지가 0
                pageRequestDTO.getSize(),
                Sort.by("tno").descending()
        );

        Page<Todo> result = todoRepository.findAll(pageable);

        List<TodoDTO> dtoList = result.getContent().stream()
                .map(todo -> modelMapper.map(todo, TodoDTO.class))
                .collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO.<TodoDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(totalCount)
                .build();

        return responseDTO;
    }



}
