package com.test.mallapi.service;


import com.test.mallapi.dto.PageRequestDTO;
import com.test.mallapi.dto.PageResponseDTO;
import com.test.mallapi.dto.ProductDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

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



    @Test
    public void testRegister() {
        ProductDTO productDTO = ProductDTO.builder()
                .pname("새로운 상품")
                .pdesc("신규 추가 상품입니다")
                .price(1000)
                .build();


        //  UUID가 있어야 한다.
        productDTO.setUploadFileNames(List.of(
                UUID.randomUUID() + "_" + "Test1.jpg",
                UUID.randomUUID() + "_" + "Test2.jpg"));

        productService.register(productDTO);
    }
}
