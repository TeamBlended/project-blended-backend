package com.gdsc.blended.jwt.repository;

import com.gdsc.blended.jwt.token.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    @Override
    Optional<RefreshTokenEntity> findById(Long aLong);
}
