package rim.bank_management_system.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DepositRequest {
    private String iban;
    private BigDecimal amount;
}

