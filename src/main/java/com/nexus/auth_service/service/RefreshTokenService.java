package com.nexus.auth_service.service;

import com.nexus.auth_service.entity.User;
import com.nexus.auth_service.entity.RefreshToken;
import com.nexus.auth_service.repository.RefreshTokenRepository;
import com.nexus.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    // application.properties dosyasından süreyi çeker
    @Value("${nexus.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // Yeni bir Refresh Token üretir ve veritabanına kaydeder
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        // Kullanıcıya ait eski bir token var mı? Varsa siliyoruz.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + userId));

        refreshTokenRepository.deleteByUser(user);

        // İşlemlerin veritabanına hemen yansıması için flush gerekebilir
        refreshTokenRepository.flush();

        // Yeni token'ı oluşturma.
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    // Token'ın süresinin dolup dolmadığını kontrol eder
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}