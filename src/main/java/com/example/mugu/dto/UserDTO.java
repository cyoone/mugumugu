package com.example.mugu.dto;

import com.example.mugu.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long user_id;
    private String phone;
    private String pwd;
    private String nickname;

    public static UserDTO toUserDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_id(userEntity.getUser_id());
        userDTO.setPhone(userEntity.getPhone());
        userDTO.setPwd(userEntity.getPwd());
        userDTO.setNickname(userEntity.getNickname());
        return userDTO;
    }
}