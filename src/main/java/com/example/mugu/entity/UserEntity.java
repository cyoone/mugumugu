package com.example.mugu.entity;

import com.example.mugu.dto.UserDTO;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long User_id;

    @Column(unique = true)  // unique 제약조건 추가
    private String phone;

    @Column
    private String pwd;

    @Column
    private String nickname;

    public static UserEntity toUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setPwd(userDTO.getPwd());
        userEntity.setNickname(userDTO.getNickname());
        return userEntity;
    }

    public static UserEntity toUpdateUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUser_id(userDTO.getUser_id());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setPwd(userDTO.getPwd());
        userEntity.setNickname(userDTO.getNickname());
        return userEntity;
    }

}
