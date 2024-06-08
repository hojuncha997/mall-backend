

package com.test.mallapi.service;


import com.test.mallapi.domain.Product;
import com.test.mallapi.domain.ProductImage;
import com.test.mallapi.dto.PageRequestDTO;
import com.test.mallapi.dto.PageResponseDTO;
import com.test.mallapi.dto.ProductDTO;
import com.test.mallapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        log.info("getList............");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());


        // ProductRepository를 통해서 Page(Object[]) 타입의 결과 데이터를 가져온다.
        Page<Object[]> result = productRepository.selectList(pageable);


        // 반복처리로 Product와 ProductImage를 ProductDTO 타입으로 변환한다.

        List<ProductDTO> dtoList = result.get().map(arr -> {

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            ProductDTO productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));

            return productDTO;

        }).collect(Collectors.toList());


        long totalCount = result.getTotalElements();
        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}


/*
 * ProductRepository를 통해서 Page(Object[]) 타입의 결과 데이터를 가져온다.
 * 각 Object[]는 Product와 ProductImage를 담고 있다.
 * 반복처리로 Product와 ProductImage를 ProductDTO 타입으로 변환한다.
 * 변환된 ProductDTO를 List<ProductDTO>로 처리하고 전체 데이터의 개수를 이용해서 PageResponseDTO를 생성하고 반환한다.
 *
 * */
