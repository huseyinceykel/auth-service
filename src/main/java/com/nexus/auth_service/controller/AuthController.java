package com.nexus.auth_service.controller;

import com.nexus.auth_service.dto.*;
import com.nexus.auth_service.entity.RefreshToken;
import com.nexus.auth_service.security.JwtUtils;
import com.nexus.auth_service.service.RefreshTokenService;
import com.nexus.auth_service.service.AuthService;
import com.nexus.auth_service.service.BlacklistService;
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
    private final BlacklistService blacklistService;

    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService,
                          JwtUtils jwtUtils,
                          BlacklistService blacklistService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
        this.blacklistService = blacklistService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(jwtResponse.getId());
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
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            blacklistService.addTokenToBlacklist(token);
            return ResponseEntity.ok("Başarıyla çıkış yapıldı ve token kara listeye alındı!");
        }
        return ResponseEntity.badRequest().body("Geçersiz Authorization header!");
    }
}