package com.gdsc.blended.user.service;


import com.gdsc.blended.common.message.UserResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserEntity updateUserNickname(String email, String newNickname){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(UserResponseMessage.NICKNAME_UPDATE_SUCCESS));
        
        if(userRepository.existsByNickname(newNickname)){
            throw new ApiException(UserResponseMessage.NICKNAME_IS_DUPLICATED);
        }
        user.setNickname(newNickname);
        return userRepository.save(user);
    }


    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(UserResponseMessage.USER_NOT_FOUND));
    }
}
