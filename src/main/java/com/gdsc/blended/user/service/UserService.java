package com.gdsc.blended.user.service;


import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity updateUserNickname(String email, String newNickname){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("유저가 정보가 없습니다." + email));
        user.setNickname(newNickname);
        return userRepository.save(user);
    }

}
