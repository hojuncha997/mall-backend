package com.test.mallapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {

    private Long tno;
    private String title;
    private String writer;
    private boolean complete;

    // JsonFormat을 사용해서 '2024-05-19'와 같은 포맷 사용
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd") mm은 는 분을 나타내므로 MM으로 변경
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    private LocalDate dueDate;
}


