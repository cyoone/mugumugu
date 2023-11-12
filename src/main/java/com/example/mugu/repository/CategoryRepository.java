package com.example.mugu.repository;

import com.example.mugu.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
