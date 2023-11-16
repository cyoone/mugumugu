package com.example.mugu.repository;

import com.example.mugu.entity.ProductEntity;
import com.example.mugu.entity.ProductFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFileEntity, Long> {
    List<ProductFileEntity> findByProduct(ProductEntity productEntity);
    ProductFileEntity findBySaveName(String save_name);
}

