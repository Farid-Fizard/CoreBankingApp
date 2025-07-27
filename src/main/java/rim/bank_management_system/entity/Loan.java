package rim.bank_management_system.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import rim.bank_management_system.entity.embedded.LoanApplicationDetails;;


@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "term_in_months", nullable = false)
    private Integer termInMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount account;

    @Embedded
    private LoanApplicationDetails applicationDetails;

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", termInMonths=" + termInMonths +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", account=" + account +
                ", applicationDetails=" + applicationDetails +
                '}';
    }
}
