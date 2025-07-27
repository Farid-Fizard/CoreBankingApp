package rim.bank_management_system.service.serviceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rim.bank_management_system.dto.response.RefreshTokenResponse;
import rim.bank_management_system.entity.RefreshToken;
import rim.bank_management_system.entity.User;
import rim.bank_management_system.exceptions.InvalidStatusException;
import rim.bank_management_system.repository.RefreshTokenRepository;
import rim.bank_management_system.service.RefreshTokenService;
import rim.bank_management_system.util.JwtUtil;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${security.jwt.refresh-token-expiration-time}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(new Date(System.currentTimeMillis() + refreshTokenDurationMs))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public RefreshTokenResponse refreshAccessToken(String refreshToken) {
        Optional<RefreshToken> storedToken = refreshTokenRepository.findByToken(refreshToken);

        if (storedToken.isEmpty() || storedToken.get().isRevoked() || storedToken.get().getExpiryDate().before(new Date())) {
            throw new InvalidStatusException("Invalid or expired refresh token");
        }

        RefreshToken existingToken = storedToken.get();
        User user = existingToken.getUser();

        existingToken.setRevoked(true);
        refreshTokenRepository.save(existingToken);

        String newAccessToken = jwtUtil.generateToken(user);
        RefreshToken newRefreshToken = createRefreshToken(user);

        return new RefreshTokenResponse(newAccessToken, newRefreshToken.getToken());
    }

}
