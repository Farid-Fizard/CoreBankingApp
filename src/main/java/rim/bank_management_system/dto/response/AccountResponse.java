package rim.bank_management_system.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponse {
    private Long id;
    private String iban;
    private BigDecimal balance;
    private String status;
    private LocalDateTime createdAt;
}

