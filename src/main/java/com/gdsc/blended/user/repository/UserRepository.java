package com.gdsc.blended.user.repository;

import com.gdsc.blended.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByNickname(String newNickname);

    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByNickname(String nickname);

}
