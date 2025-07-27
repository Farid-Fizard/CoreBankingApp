package rim.bank_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private BankAccount fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private BankAccount toAccount;

    private String toIban;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean successful;
}
