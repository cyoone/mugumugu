package com.example.mugu.dto;

import com.example.mugu.entity.ProductFileEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductFileDTO {
    private Long file_id;
    private String original_name;
    private String save_name;
    private Integer is_valid;
    private String created_at;
    private String updated_at;

    private ProductDTO product;
    private String product_name;


    public ProductFileDTO(String original_name, String save_name, Integer is_valid) {
        this.original_name = original_name;
        this.save_name = save_name;
        this.is_valid = is_valid;
    }



    public static ProductFileDTO toProductFileDTO(ProductFileEntity productFileEntity) {
        if (productFileEntity == null) {
            return null;
        }
        ProductFileDTO productFileDTO = new ProductFileDTO();
        productFileDTO.setFile_id(productFileEntity.getFile_id());
        productFileDTO.setOriginal_name(productFileEntity.getOriginal_name());
        productFileDTO.setSave_name(productFileEntity.getSaveName());
        productFileDTO.setCreated_at(productFileEntity.getCreatedAt());
        productFileDTO.setUpdated_at(productFileEntity.getUpdated_at());

        return productFileDTO;
    }
}
