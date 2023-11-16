package com.example.mugu.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.mugu.configuration.S3Config;
import com.example.mugu.dto.CategoryDTO;
import com.example.mugu.dto.ProductDTO;
import com.example.mugu.dto.ProductFileDTO;
import com.example.mugu.entity.CategoryEntity;
import com.example.mugu.entity.ProductEntity;
import com.example.mugu.entity.ProductFileEntity;
import com.example.mugu.repository.ProductFileRepository;
import com.example.mugu.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductFileRepository productFileRepository;
    private final S3Config s3Config;

    // s3에 이미지 업로드
    public String uploadFileToS3(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String saveName = UUID.randomUUID().toString() + "_" + originalName;

        try {
            s3Config.upload(file.getInputStream(), saveName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s3Config.getUrl(saveName);
    }

    // 이미지 가져오기
    public List<String> getImageUrls(ProductEntity product) {
        // ProductFileEntity 조회
        List<ProductFileEntity> productFileEntities = productFileRepository.findByProduct(product);

        // is_valid 값이 1인 이미지만 필터링
        List<String> urls = productFileEntities.stream()
                .filter(productFileEntity -> productFileEntity.getIs_valid() == 1)
                .map(ProductFileEntity::getSaveName)
                .collect(Collectors.toList());

        return urls;
    }

//    public void deleteImage(Long productFileId) {
//        // ProductFileEntity에서 이미지 정보를 가져옴
//        Optional<ProductFileEntity> optionalProductFileEntity = productFileRepository.findById(productFileId);
//
//        if (optionalProductFileEntity.isPresent()) {
//            ProductFileEntity productFileEntity = optionalProductFileEntity.get();
//
//            // S3에서 이미지 삭제
//            String saveName = productFileEntity.getSave_name();
//            s3Config.delete(saveName);
//
//            // 데이터베이스에서 이미지 정보 삭제
//            productFileRepository.delete(productFileEntity);
//        } else {
//            return;
//        }
//    }

    @Transactional
    public void deleteImageByUrl(String url) {

        // 데이터베이스에서 이미지 정보를 찾음
        ProductFileEntity productFileEntity = productFileRepository.findBySaveName(url);

        if (productFileEntity != null) {
            // S3에서 이미지 삭제
            //s3Config.delete(fileName);

            // is_valid 값을 2로 변경
            productFileEntity.setIs_valid(2);
            productFileRepository.save(productFileEntity);
        }
    }


    public void deleteImage(ProductFileEntity productFileEntity) {
            // S3에서 이미지 삭제
//            String saveName = productFileEntity.getSaveName();
//            String fileName = saveName.substring(saveName.lastIndexOf("/") + 1); // URL에서 파일 이름만 추출
//            s3Config.delete(fileName);

            // 데이터베이스에서 이미지 정보 삭제
//            productFileRepository.delete(productFileEntity);
            // 데이터베이스에서 이미지 정보의 is_valid 값을 2로 변경
            productFileEntity.setIs_valid(2);
            productFileRepository.save(productFileEntity);
        }


    public void addNewImages(Long product_id, List<MultipartFile> newImages) {
        if (newImages != null) {
            for (MultipartFile file : newImages) {
                String url = uploadFileToS3(file);

                // ProductFileEntity 생성
                ProductFileEntity productFileEntity = new ProductFileEntity();
                productFileEntity.setOriginal_name(file.getOriginalFilename());
                productFileEntity.setSaveName(url);
                productFileEntity.setIs_valid(1);

                // ProductEntity를 찾아서 설정
                ProductEntity productEntity = productRepository.findById(product_id)
                        .orElseThrow(() -> new NotFoundException("Product not found with id: " + product_id));
                productFileEntity.setProduct(productEntity);


                // ProductFileEntity 저장
                productFileRepository.save(productFileEntity);
            }
        }
    }




    public void save(ProductDTO productDTO) {
        ProductEntity productEntity = ProductEntity.toSaveEntity(productDTO);
        productRepository.save(productEntity);

        // 이미지 파일을 S3에 업로드하고, 업로드된 이미지의 URL을 ProductFileEntity에 저장
        for (MultipartFile file : productDTO.getFiles()) {
            String url = uploadFileToS3(file);

            // ProductFileEntity 생성
            ProductFileEntity productFileEntity = new ProductFileEntity();
            productFileEntity.setOriginal_name(file.getOriginalFilename());
            productFileEntity.setSaveName(url);
            productFileEntity.setIs_valid(1);
            productFileEntity.setProduct(productEntity);

            // ProductFileEntity 저장
            productFileRepository.save(productFileEntity);
        }
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

            // 이미지 URL 가져오기
            List<String> urls = getImageUrls(productEntity);
            productDTO.setFileUrls(urls);

            return productDTO;


        } else {
            return null;
        }
    }

    @Transactional
    public ProductDTO update(Long product_id, ProductDTO productDTO) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(product_id);

        if(optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();

            productEntity.setName(productDTO.getName());
            productEntity.setPrice(productDTO.getPrice());
            productEntity.setComment(productDTO.getComment());
            productEntity.setTrade_loc(productDTO.getTrade_loc());

            // 카테고리 정보 업데이트
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setCategory_id(productDTO.getCategory_id());
            productEntity.setCategory(categoryEntity);


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


            // 상품에 연관된 이미지들을 찾아 S3와 데이터베이스에서 삭제
//            List<ProductFileEntity> productFileEntities = productFileRepository.findByProduct(productEntity);
//            for (ProductFileEntity productFileEntity : productFileEntities) {
//                String saveName = productFileEntity.getSave_name();
//                s3Config.delete(saveName); // S3에서 이미지 삭제
//                productFileRepository.delete(productFileEntity); // 데이터베이스에서 이미지 정보 삭제
//            }
            // 상품에 연관된 이미지들을 찾아 S3와 데이터베이스에서 삭제
            List<ProductFileEntity> productFileEntities = productFileRepository.findByProduct(productEntity);
            for (ProductFileEntity productFileEntity : productFileEntities) {
                deleteImage(productFileEntity); // 이미지 삭제
            }



            productEntity.setStatus(2);
            productRepository.save(productEntity);
        } else {
            return;
        }
    }

    public void hide(Long product_id) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(product_id);
        if (optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();
            productEntity.setStatus(0);
            productRepository.save(productEntity);
        } else {
            return;
        }
    }

    public void unhide(Long product_id) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(product_id);
        if (optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();
            productEntity.setStatus(1);
            productRepository.save(productEntity);
        } else {
            return;
        }
    }

    public Page<ProductDTO> paging(Pageable pageable, String category, String name, String sort) {
        int page = pageable.getPageNumber() - 1;    // page 위치에 있는 값은 0부터 시작
        int pageLimit = 3;  // 한 페이지 당 보여줄 상품 갯수

        // 상품 상태가 1인 상품만 노출, 카테고리 및 검색에 따른 필터링
        Specification<ProductEntity> spec = (root, query, cb) -> {
            Predicate statusPredicate = cb.equal(root.get("status"), 1);
            Predicate categoryPredicate = (category != null && !category.isEmpty()) ? cb.equal(root.get("category").get("name"), category) : cb.conjunction();
            Predicate namePredicate = (name != null && !name.isEmpty()) ? cb.like(root.get("name"), "%" + name + "%") : cb.conjunction();
            return cb.and(statusPredicate, categoryPredicate, namePredicate);
        };

        // page: 몇 페이지를 보고싶은지   Sort.by: 어떤 기준으로 정렬할지(내림차순, 엔티티에 작성한 이름)
        Page<ProductEntity> productEntities =
                productRepository.findAll(spec, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, sort)));

        System.out.println("productEntities.getContent() = " + productEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("productEntities.getTotalElements() = " + productEntities.getTotalElements()); // 전체 글갯수
        System.out.println("productEntities.getNumber() = " + productEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("productEntities.getTotalPages() = " + productEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("productEntities.getSize() = " + productEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("productEntities.hasPrevious() = " + productEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("productEntities.isFirst() = " + productEntities.isFirst()); // 첫 페이지 여부
        System.out.println("productEntities.isLast() = " + productEntities.isLast()); // 마지막 페이지 여부

        // 리스트에서 보여줄 내용: id(추후삭제), name, price, 동네(user랑 연결 후 추가), views, likes
        // 엔티티 -> DTO
//        Page<ProductDTO> productDTOS = productEntities.map(product -> new ProductDTO(product.getProduct_id(), product.getName(), product.getPrice(), product.getStatus(), product.getViews(), product.getLikes()));
//        return productDTOS;
        Page<ProductDTO> productDTOS = productEntities.map(product -> {

            // 이미지 URL 가져오기
            List<String> urls = getImageUrls(product);

            // ProductDTO 생성
            ProductDTO productDTO = new ProductDTO(product.getProduct_id(), product.getName(), product.getPrice(), product.getStatus(), product.getViews(), product.getLikes());
            productDTO.setFileUrls(urls);

            return productDTO;
        });
        return productDTOS;


    }


}
