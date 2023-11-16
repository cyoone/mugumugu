package com.example.mugu.dto;

import com.example.mugu.entity.CategoryEntity;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long category_id;
    private String name;
    private Integer is_valid;
    private String created_at;
    private String updated_at;

    public static CategoryDTO toCategoryDTO(CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategory_id(categoryEntity.getCategory_id());
        categoryDTO.setName(categoryEntity.getName());
        categoryDTO.setIs_valid(categoryEntity.getIs_valid());
        categoryDTO.setCreated_at(categoryEntity.getCreatedAt());
        categoryDTO.setUpdated_at(categoryEntity.getUpdated_at());
        return categoryDTO;
    }
}
