package rim.bank_management_system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rim.bank_management_system.dto.request.LoginRequest;
import rim.bank_management_system.dto.request.RefreshTokenRequest;
import rim.bank_management_system.dto.request.RegisterRequest;
import rim.bank_management_system.dto.response.LoginResponse;
import rim.bank_management_system.dto.response.RefreshTokenResponse;
import rim.bank_management_system.service.AuthService;
import rim.bank_management_system.service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Handles authentication operations")

@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = refreshTokenService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
