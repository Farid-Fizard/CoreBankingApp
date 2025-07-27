package rim.bank_management_system.service;

import rim.bank_management_system.dto.request.LoginRequest;
import rim.bank_management_system.dto.response.LoginResponse;
import rim.bank_management_system.dto.request.RegisterRequest;

public interface AuthService {
    LoginResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
