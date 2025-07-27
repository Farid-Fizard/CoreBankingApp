package rim.bank_management_system.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    private Long fromAccountId;
    BigDecimal amount;
}

