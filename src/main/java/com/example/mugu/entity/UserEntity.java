package com.example.mugu.entity;

import com.example.mugu.dto.UserDTO;
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
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Integer user_id;

    @Column(unique = true)  // unique 제약조건 추가
    private String phone;

    @Column
    private String pwd;

    @Column
    private String nickname;

    @Column
    private Integer manner;

    @Column
    private Integer status;

    @CreatedDate
    @Column(updatable = false)
    private String created_at;

    @CreatedDate
    @Column(updatable = true)
    private String updated_at;

    @PrePersist
    public void onPrePersist() {
        this.created_at = String.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        this.updated_at = String.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    public static UserEntity toUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setPwd(userDTO.getPwd());
        userEntity.setNickname(userDTO.getNickname());
        userEntity.setManner(userDTO.getManner());
        userEntity.setStatus(userDTO.getStatus());
        userEntity.setCreated_at(userDTO.getCreated_at());
        userEntity.setUpdated_at(userDTO.getUpdated_at());
        return userEntity;
    }

    public static UserEntity toUpdateUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUser_id(userDTO.getUser_id());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setPwd(userDTO.getPwd());
        userEntity.setNickname(userDTO.getNickname());
        userEntity.setManner(userDTO.getManner());
        userEntity.setStatus(userDTO.getStatus());
        userEntity.setCreated_at(userDTO.getCreated_at());
        userEntity.setUpdated_at(userDTO.getUpdated_at());
        return userEntity;
    }

}
