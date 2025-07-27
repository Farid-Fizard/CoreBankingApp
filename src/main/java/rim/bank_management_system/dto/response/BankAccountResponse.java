package rim.bank_management_system.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountResponse {
    private Long id;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private String status;
}
