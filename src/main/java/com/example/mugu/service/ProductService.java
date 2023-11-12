package com.example.mugu.service;

import com.example.mugu.dto.CategoryDTO;
import com.example.mugu.dto.ProductDTO;
import com.example.mugu.entity.CategoryEntity;
import com.example.mugu.entity.ProductEntity;
import com.example.mugu.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public void save(ProductDTO productDTO) {
        ProductEntity productEntity = ProductEntity.toSaveEntity(productDTO);
        productRepository.save(productEntity);
    }

    @Transactional
    public List<ProductDTO> findAll() {
        List<ProductEntity> productEntityList = productRepository.findAllWithCategory();
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (ProductEntity productEntity: productEntityList) {
            productDTOList.add(ProductDTO.toProductDTO(productEntity));
        }
        return productDTOList;
    }

    @Transactional
    public void updateViews(Long product_id) {
        productRepository.updateViews(product_id);
    }

    @Transactional
    public ProductDTO findById(Long product_id) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(product_id);

        if (optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();
            ProductDTO productDTO = ProductDTO.toProductDTO(productEntity);

            // category 설정
            CategoryEntity categoryEntity = productEntity.getCategory();
            if (categoryEntity != null) {
                CategoryDTO categoryDTO = new CategoryDTO();
                // categoryDTO 필드 설정
                productDTO.setCategory(categoryDTO);
            }
            return productDTO;
        } else {
            return null;
        }
    }

//    public ProductDTO update(ProductDTO productDTO) {
//        Optional<ProductEntity> optionalProductEntity = productRepository.findById(productDTO.getProduct_id());
//
//        if (optionalProductEntity.isPresent()) {
//            ProductEntity productEntity = ProductEntity.toUpdateEntity(productDTO);
//
//            productRepository.save(productEntity);
//            return findById(productDTO.getProduct_id());
//        } else {
//            return null;
//        }
//    }

    @Transactional
        public ProductDTO update(Long product_id, ProductDTO productDTO) {
            Optional<ProductEntity> optionalProductEntity = productRepository.findById(product_id);

            if(optionalProductEntity.isPresent()) {
                ProductEntity productEntity = optionalProductEntity.get();

                productEntity.setName(productDTO.getName());
                productEntity.setPrice(productDTO.getPrice());
                productEntity.setComment(productDTO.getComment());
                productEntity.setTrade_loc(productDTO.getTrade_loc());

                // 카테고리 설정
//                Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(productDTO.getCategory_id());
//                if (optionalCategoryEntity.isPresent()) {
//                    CategoryEntity categoryEntity = optionalCategoryEntity.get();
//                    productEntity.setCategory(categoryEntity);
//                } else {
//                    // 적절한 오류 처리를 수행합니다.
//                }

                // 카테고리 설정
//                CategoryEntity categoryEntity = new CategoryEntity();
////                categoryEntity.setCategory_id(productDTO.getCategory_id());
//                productEntity.setCategory(categoryEntity);
//
//                ProductEntity.updateEntity(productEntity, productDTO);
//                productRepository.save(productEntity);
//            } else {
//                // 적절한 오류 처리를 수행합니다.
//            }

                productRepository.save(productEntity);
                return findById(productDTO.getProduct_id());
            }   else {
                // 존재하지 않는 경우에 대한 처리
                return null;
            }

        }

        public void delete(Long product_id) {
            Optional<ProductEntity> optionalProductEntity = productRepository.findById(product_id);
            if (optionalProductEntity.isPresent()) {
                ProductEntity productEntity = optionalProductEntity.get();
                productEntity.setStatus(2);
                productRepository.save(productEntity);
            } else {

            }
        }


}
