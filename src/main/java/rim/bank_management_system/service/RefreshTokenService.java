package rim.bank_management_system.service;

import rim.bank_management_system.dto.response.RefreshTokenResponse;
import rim.bank_management_system.entity.RefreshToken;
import rim.bank_management_system.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshTokenResponse refreshAccessToken(String refreshToken);
}
