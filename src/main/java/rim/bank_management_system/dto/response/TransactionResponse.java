package rim.bank_management_system.dto.response;

import lombok.*;
import rim.bank_management_system.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private String toIban;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime timestamp;
    private boolean successful;
}

