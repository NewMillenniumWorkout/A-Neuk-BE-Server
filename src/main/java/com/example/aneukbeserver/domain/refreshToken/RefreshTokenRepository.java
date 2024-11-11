package com.example.aneukbeserver.domain.refreshToken;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    // accessToken으로 refreshToken 찾기
    Optional<RefreshToken> findByAccessToken(String accessToken);
}
