package com.example.mugu.repository;

import com.example.mugu.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.category")
    List<ProductEntity> findAllWithCategory();

    @Modifying
    @Query(value = "update ProductEntity p set p.views=p.views+1 where p.product_id=:product_id")
    void updateViews(@Param("product_id") Long product_id);
}
