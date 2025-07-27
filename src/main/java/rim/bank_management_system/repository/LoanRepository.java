package rim.bank_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rim.bank_management_system.entity.Loan;
import rim.bank_management_system.entity.LoanStatus;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByAccountUserUsername(String username);
    boolean existsByAccountIdAndStatus(Long accountId, LoanStatus status);
}

