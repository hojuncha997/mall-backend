package com.test.mallapi.service;


import com.test.mallapi.dto.PageRequestDTO;
import com.test.mallapi.dto.PageResponseDTO;
import com.test.mallapi.dto.ProductDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Test
    public void testList() {

    // 1 page, 10 size
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<ProductDTO> result = productService.getList(pageRequestDTO);

        result.getDtoList().forEach(dto -> log.info(dto));
    }
}
