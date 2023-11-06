package com.example.mugu.dto;

import com.example.mugu.entity.UserEntity;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private BigInteger user_id;
    private String phone;
    private String pwd;
    private String nickname;
    private Integer manner;
    private Integer status;
    private String created_at;
    private String updated_at;

    public static UserDTO toUserDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_id(userEntity.getUser_id());
        userDTO.setPhone(userEntity.getPhone());
        userDTO.setPwd(userEntity.getPwd());
        userDTO.setNickname(userEntity.getNickname());
        userDTO.setManner(userEntity.getManner());
        userDTO.setStatus(userEntity.getStatus());
        userDTO.setCreated_at(userEntity.getCreated_at());
        userDTO.setUpdated_at(userEntity.getUpdated_at());
        return userDTO;
    }
}