package com.example.mugu.dto;

import com.example.mugu.entity.ProductEntity;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long product_id;

    @Size(max = 40, message = "상품 이름은 최대 40자까지 입력 가능합니다.")
    private String name;

    @Size(max = 9, message = "상품 가격은 최대 9자까지 입력 가능합니다.")
    private Integer price;

    @Size(max = 2000, message = "상품 설명은 최대 2000자까지 입력 가능합니다.")
    private String comment;

    private Integer status;
    private Integer views;
    private Integer likes;

    @Size(max = 40, message = "상품 거래 위치는 최대 40자까지 입력 가능합니다.")
    private String trade_loc;

    private String created_at;
    private String updated_at;

    private CategoryDTO category;
    private Long category_id;
    private String category_name;

    //name, price, (동네), views, likes
    public ProductDTO(Long product_id, String name, int price, int views, int likes) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.views = views;
        this.likes = likes;
    }

    public static ProductDTO toProductDTO(ProductEntity productEntity) {
        if (productEntity == null) {
            return null; // 또는 적절한 처리를 수행하거나 예외를 던집니다.
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProduct_id(productEntity.getProduct_id());
        productDTO.setName(productEntity.getName());
        productDTO.setPrice(productEntity.getPrice());
        productDTO.setComment(productEntity.getComment());
        productDTO.setStatus(productEntity.getStatus());
        productDTO.setViews(productEntity.getViews());
        productDTO.setLikes(productEntity.getLikes());
        productDTO.setTrade_loc(productEntity.getTrade_loc());
        productDTO.setCreated_at(productEntity.getCreated_at());
        productDTO.setUpdated_at(productEntity.getUpdated_at());

        // 카테고리 이름
        if (productEntity.getCategory() != null) {
            productDTO.setCategory_name(productEntity.getCategory().getName());
        }

        return productDTO;
    }


}
