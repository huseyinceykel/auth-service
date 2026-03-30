package com.nexus.auth_service.controller;

import com.nexus.auth_service.dto.*;
import com.nexus.auth_service.entity.RefreshToken;
import com.nexus.auth_service.security.JwtUtils;
import com.nexus.auth_service.service.RefreshTokenService;
import com.nexus.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Kullanıcıyı doğrula ve Access Token al
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);

        // Bu kullanıcı için bir Refresh Token üret
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(jwtResponse.getId());

        // JwtResponse içine refresh token'ı setle
        jwtResponse.setRefreshToken(refreshToken.getToken());

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        String message = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    // Kullanıcı adına göre yeni bir Access Token üret
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}