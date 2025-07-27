package rim.bank_management_system.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationDetails {

    @Column(name = "monthly_income", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyIncome;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "credit_score", nullable = false)
    private Integer creditScore;

    @Column(name = "has_guarantor", nullable = false)
    private boolean hasGuarantor;
}
