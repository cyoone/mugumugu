package com.example.mugu.service;

import com.example.mugu.dto.CategoryDTO;
import com.example.mugu.entity.CategoryEntity;
import com.example.mugu.repository.CategoryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public void save(CategoryDTO categoryDTO) {
        // 1. dto -> entity 변환
        // 2. repository의 save 메서드 호출
        CategoryEntity categoryEntity = CategoryEntity.toSaveEntity(categoryDTO);
        categoryRepository.save(categoryEntity);
    }

    @Transactional
    public List<CategoryDTO> findAll() {
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (CategoryEntity categoryEntity: categoryEntityList) {
            categoryDTOList.add(CategoryDTO.toCategoryDTO(categoryEntity));
        }
        return categoryDTOList;
    }

    @Transactional
    public CategoryDTO findById(Long category_id) {
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(category_id);
        if (optionalCategoryEntity.isPresent()) {
            CategoryEntity categoryEntity = optionalCategoryEntity.get();
            CategoryDTO categoryDTO = CategoryDTO.toCategoryDTO(categoryEntity);
            return categoryDTO;
            //return CategoryDTO.toCategoryDTO(optionalCategoryEntity.get());
        } else {
            return null;
        }
    }

//    public CategoryDTO update(CategoryDTO categoryDTO) {
//        CategoryEntity categoryEntity = CategoryEntity.toUpdateEntity(categoryDTO);
//        categoryRepository.save(categoryEntity);
//        return findById(categoryDTO.getCategory_id());
//    }

    public CategoryDTO update(CategoryDTO categoryDTO) {
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(categoryDTO.getCategory_id());

        if (optionalCategoryEntity.isPresent()) {
            CategoryEntity existingEntity = optionalCategoryEntity.get();
            existingEntity.setName(categoryDTO.getName());
            existingEntity.setIs_valid(categoryDTO.getIs_valid());
//            existingEntity.setCreated_at(categoryDTO.getCreated_at());
//            existingEntity.setUpdated_at(categoryDTO.getUpdated_at());

            categoryRepository.save(existingEntity);

            return findById(categoryDTO.getCategory_id());
        } else {
            // 존재하지 않는 경우에 대한 처리
            return null;
        }
    }


    public void delete(Long category_id) {
//        categoryRepository.deleteById(category_id);
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById(category_id);

        if (optionalCategoryEntity.isPresent()) {
            CategoryEntity existingEntity = optionalCategoryEntity.get();
            existingEntity.setIs_valid(2); // is_valid 값을 2로 변경
            categoryRepository.save(existingEntity); // 변경된 값을 저장
        } else {
            // 존재하지 않는 경우에 대한 처리
        }
    }

}
