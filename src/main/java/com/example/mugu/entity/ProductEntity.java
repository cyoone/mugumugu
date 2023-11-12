package com.example.mugu.entity;

import com.example.mugu.dto.ProductDTO;
import com.example.mugu.service.CategoryService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Setter
@Getter
@Table(name = "product")
public class ProductEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long product_id;


    @Column
    private String name;

    @Column
    private int price;

    @Column
    private String comment;

    @Column
    private int status;

    @Column
    private int views;

    @Column
    private int likes;

    @Column
    private String trade_loc;

    // 카테고리 테이블 참조
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;


    // DTO -> Entity
    public static ProductEntity toSaveEntity(ProductDTO productDTO) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(productDTO.getName());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setComment(productDTO.getComment());
        productEntity.setStatus(1);
        productEntity.setViews(0);
        productEntity.setLikes(0);
        productEntity.setTrade_loc(productDTO.getTrade_loc());

        // 카테고리 설정
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategory_id(productDTO.getCategory_id());
        productEntity.setCategory(categoryEntity);
        return productEntity;
    }

    // 업데이트
    public static ProductEntity toUpdateEntity(ProductDTO productDTO) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProduct_id(productDTO.getProduct_id());
        productEntity.setName(productDTO.getName());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setComment(productDTO.getComment());
        productEntity.setStatus(productDTO.getStatus());
        productEntity.setViews(productDTO.getViews());
        productEntity.setLikes(productDTO.getLikes());
        productEntity.setTrade_loc(productDTO.getTrade_loc());

        // 카테고리 설정
//        CategoryEntity categoryEntity = new CategoryEntity();
//        categoryEntity.setCategory_id(productDTO.getCategory_id());
//        productEntity.setCategory(categoryEntity);

        return productEntity;
    }

    // DTO -> Entity
//    public static void updateEntity(ProductEntity productEntity, ProductDTO productDTO) {
//
//        productEntity.setName(productDTO.getName());
//        productEntity.setPrice(productDTO.getPrice());
//        productEntity.setComment(productDTO.getComment());
//        productEntity.setTrade_loc(productDTO.getTrade_loc());
//
////
//        if (productDTO.getCategoryEntity() != null) {
//            productEntity.setCategory(productDTO.getCategoryEntity());
//        }
        // 카테고리 설정
//        if (productEntity.getCategory() == null || !productEntity.getCategory().getCategory_id().equals(productDTO.getCategory_id())) {
//            CategoryEntity categoryEntity = new CategoryEntity();
//            categoryEntity.setCategory_id(productDTO.getCategory_id());
//            productEntity.setCategory(categoryEntity);
//        }

        // 카테고리 설정
//        CategoryEntity categoryEntity = new CategoryEntity();
//        categoryEntity.setCategory_id(productDTO.getCategory_id());
//        categoryEntity.setName(productDTO.getCategory_name());  // 추가된 코드
//        productEntity.setCategory(categoryEntity);

//    }

}
