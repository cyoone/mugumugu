package com.example.mugu.service;

import com.example.mugu.dto.UserDTO;
import com.example.mugu.entity.UserEntity;
import com.example.mugu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void signup(UserDTO userDTO) {
        // 1. dto -> entity 변환
        // 2. repository의 save 메서드 호출
        UserEntity userEntity = UserEntity.toUserEntity(userDTO);
        userRepository.save(userEntity);    // 이 메소드는 무조건 save를 써야함
        // repository의 save메서드 호출 (조건. entity객체를 넘겨줘야 함)
    }

    public UserDTO signin(UserDTO userDTO) {
        /*
            1. 회원이 입력한 이메일로 DB에서 조회를 함
            2. DB에서 조회한 비밀번호화 사용자가 입력한 비밀번호가 일치하는지 판단
         */
        Optional<UserEntity> byUserPhone = userRepository.findByphone(userDTO.getPhone());
        if (byUserPhone.isPresent()) {
            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            UserEntity userEntity = byUserPhone.get();
            if (userEntity.getPwd().equals(userDTO.getPwd())) {
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                UserDTO dto = UserDTO.toUserDTO(userEntity);
                return dto;
            } else {
                // 비밀번호 불일치(로그인 실패)
                return null;
            }
        } else {
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return null;
        }
    }

    public List<UserDTO> findAll() {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (UserEntity userEntity: userEntityList) {
            userDTOList.add(UserDTO.toUserDTO(userEntity));
//            UserDTO userDTO = UserDTO.toUserDTO(userEntity);
//            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    public UserDTO findById(Long user_id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(user_id);
        if (optionalUserEntity.isPresent()) {
//            3줄을 한줄로 표현한 것
//            UserEntity userEntity = optionalUserEntity.get();
//            UserDTO userDTO = UserDTO.toUserDTO(userEntity);
//            return userDTO;
            return UserDTO.toUserDTO(optionalUserEntity.get());
        } else {
            return null;
        }

    }

    public UserDTO updateForm(String myPhone) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByphone(myPhone);
        if (optionalUserEntity.isPresent()) {
            return UserDTO.toUserDTO(optionalUserEntity.get());
        } else  {
            return null;
        }
    }

    public void update(UserDTO userDTO) {
        userRepository.save(UserEntity.toUpdateUserEntity(userDTO));
    }

    public void deleteById(Long User_id) {
        userRepository.deleteById(User_id);
    }

    public String phoneCheck(String phone) {
        Optional<UserEntity> byUserPhone = userRepository.findByphone(phone);
        if (byUserPhone.isPresent()) {
            // 조회결과가 있다 -> 사용할 수 없다.
            return null;
        } else {
            // 조회결과가 없다 -> 사용할 수 있다.
            return "ok";
        }
    }
}
