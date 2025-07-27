package rim.bank_management_system.dto.response;

import lombok.Data;
import rim.bank_management_system.entity.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanResponse {
    private Long id;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer termInMonths;
    private LoanStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}
