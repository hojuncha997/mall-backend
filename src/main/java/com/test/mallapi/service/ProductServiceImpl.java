

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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;



    /*
     * ProductRepository를 통해서 Page(Object[]) 타입의 결과 데이터를 가져온다.
     * 각 Object[]는 Product와 ProductImage를 담고 있다.
     * 반복처리로 Product와 ProductImage를 ProductDTO 타입으로 변환한다.
     * 변환된 ProductDTO를 List<ProductDTO>로 처리하고 전체 데이터의 개수를 이용해서 PageResponseDTO를 생성하고 반환한다.
     *
     * */
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



    // 등록처리를 위해서 ProductDTO를 PRoduct와 ProductImage로 타입의 객체들로 만들어서 처리한다
    @Override
    public Long register(ProductDTO productDTO) {

            Product product = dtoToEntity(productDTO);

            Product result = productRepository.save(product);

            return result.getPno();

    }

    @Override
    public ProductDTO get(Long pno) {

        java.util.Optional<Product> result = productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        ProductDTO productDTO = entityToDTO(product);

        return productDTO;
    }

    @Override
    public void modify(ProductDTO productDTO) {

        // step1: read
        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product = result.orElseThrow();

        // step2: modify entity
        product.changeName(productDTO.getPname());
        product.changeDesc(productDTO.getPdesc());
        product.changePrice(productDTO.getPrice());

        // step3: clear existing image list
        product.clearList();

        List<String> uploadFileNames = productDTO.getUploadFileNames();

        // step4: add new image list
        if(uploadFileNames != null && uploadFileNames.size() > 0) {
            uploadFileNames.stream().forEach(uploadName -> {
                product.addImageString(uploadName);
            });
        }
        productRepository.save(product);

    }


    // dto를 엔티티로 변경
    private Product dtoToEntity(ProductDTO productDTO) {

        Product product = Product.builder()
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();

        // 업로드 처리가 끝난 파일들의 이름 리스트
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if(uploadFileNames == null) {
            // 업로드 파일이 없는 Product(엔티티) 반환.
            return product;
        }

        // 업로드 파일이 있는 경우, 엔티티에 이미지 파일들을 추가하고
        uploadFileNames.stream().forEach(uploadName -> {
            product.addImageString(uploadName);
        });

        // 엔티티를 반환
        return product;
    }

    // 엔티티를 dto로 변경
    private ProductDTO entityToDTO(Product product) {

        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.size() == 0) {
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(productImage ->
                productImage.getFileName()).toList();

        productDTO.setUploadFileNames(fileNameList);

        return productDTO;
    }




}


