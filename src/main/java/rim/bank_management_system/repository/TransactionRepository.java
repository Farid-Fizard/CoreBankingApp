package rim.bank_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rim.bank_management_system.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
