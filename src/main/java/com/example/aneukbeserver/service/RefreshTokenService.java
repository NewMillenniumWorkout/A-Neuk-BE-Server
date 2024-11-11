package com.example.aneukbeserver.service;

import com.example.aneukbeserver.domain.refreshToken.RefreshToken;
import com.example.aneukbeserver.domain.refreshToken.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveTokenInfo(String email, String refreshToken, String accessToken) {
        refreshTokenRepository.save(new RefreshToken(email, accessToken, refreshToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        RefreshToken token = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("AccessToken이 존재하지 않습니다: " + accessToken));
        refreshTokenRepository.delete(token);
    }
}
