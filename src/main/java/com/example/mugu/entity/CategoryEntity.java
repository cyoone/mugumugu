package com.example.mugu.entity;

import com.example.mugu.dto.CategoryDTO;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Setter
@Getter
@Table(name = "category")
public class CategoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long category_id;

    @Column
    private String name;

    @Column
    private int is_valid;

    // DTO 에 담긴 값들을 Entity 객체로 옮김
    public static CategoryEntity toSaveEntity(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setIs_valid(1);  // 유효
//        categoryEntity.setCreated_at(categoryDTO.getCreated_at());
//        categoryEntity.setUpdated_at(categoryDTO.getUpdated_at());
        return categoryEntity;
    }

    // 동일한 id 값을 가지는 객체를 업데이트
    public static CategoryEntity toUpdateEntity(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategory_id(categoryDTO.getCategory_id());
        categoryEntity.setName(categoryDTO.getName());
        categoryEntity.setIs_valid(categoryDTO.getIs_valid());
//        categoryEntity.setCreated_at(categoryDTO.getCreated_at());
//        categoryEntity.setUpdated_at(categoryDTO.getUpdated_at());
        return categoryEntity;
    }


}
