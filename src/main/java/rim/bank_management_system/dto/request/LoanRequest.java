package rim.bank_management_system.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanRequest {
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer termInMonths;

    // Kredit üçün müraciət edən şəxsin məlumatları
    private BigDecimal monthlySalary;
    private Integer age;
    private boolean hasGuarantor;
    private int creditScore;

    private Long accountId;
}
