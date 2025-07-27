package rim.bank_management_system.dto.response;


import lombok.Data;

@Data
public class LoginResponse {
    private String username;
    private String email;
    private String accessToken;
    private String refreshToken;
}

