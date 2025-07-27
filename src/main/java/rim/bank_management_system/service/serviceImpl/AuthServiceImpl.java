package rim.bank_management_system.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rim.bank_management_system.dto.request.LoginRequest;
import rim.bank_management_system.dto.request.RegisterRequest;
import rim.bank_management_system.dto.response.LoginResponse;
import rim.bank_management_system.entity.User;
import rim.bank_management_system.exceptions.DuplicatedUserException;
import rim.bank_management_system.exceptions.ResourceNotFoundException;
import rim.bank_management_system.repository.UserRepository;
import rim.bank_management_system.service.AuthService;
import rim.bank_management_system.service.RefreshTokenService;
import rim.bank_management_system.util.JwtUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResponse register(RegisterRequest request) {
        log.info("Register request received for username: {}", request.getUsername());
        if (userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
            log.warn("Duplicate user attempt for username: {}, email: {}", request.getUsername(), request.getEmail());
            throw new DuplicatedUserException("Username or Email already exists!");
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        log.info("New user registered successfully with username: {}", user.getUsername());
        return generateLoginResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for username: {}", request.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->{
                    log.error("Login failed for username: {}. User not found.", request.getUsername());
                    return new ResourceNotFoundException("Invalid username or password");
                });

        log.info("User login successful: {}", user.getUsername());
        return generateLoginResponse(user);
    }

    private LoginResponse generateLoginResponse(User user) {
        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();
        log.debug("Access and refresh tokens generated for user: {}", user.getUsername());

        LoginResponse response = modelMapper.map(user, LoginResponse.class);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }
}
