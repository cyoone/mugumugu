package com.example.mugu.repository;

import com.example.mugu.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <UserEntity, Long> {
    // 핸드폰번호로 회원 정보 조회 (select * from user where phone=?
    Optional<UserEntity> findByphone(String phone);
}
