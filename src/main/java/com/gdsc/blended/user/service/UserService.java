package com.gdsc.blended.user.service;


import com.gdsc.blended.user.dto.UserRequestDto;
import com.gdsc.blended.user.dto.response.UserResponseDto;
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
    public UserResponseDto updateNickname(UserRequestDto userRequestDto, String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));
        UserEntity updatedNickname = userRepository.save(user);

        return new UserResponseDto(updatedNickname);
    }
}
