package com.test.mallapi.repository;

import com.test.mallapi.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
//   JpaRepository를 상속해서 만드는 TodoRepository는 별도의 메서를 작성하지 않아도 CRUD와 페이징 처리가 가능
}
