package rim.bank_management_system.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import rim.bank_management_system.entity.BankAccount;

import java.util.List;
import java.util.Optional;


public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findAllByUserUsername(String username);

    Optional<BankAccount> findByIban(String iban);
}


