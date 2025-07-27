package rim.bank_management_system.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rim.bank_management_system.entity.AccountStatus;
import rim.bank_management_system.entity.BankAccount;
import rim.bank_management_system.exceptions.AccessDeniedBankingException;
import rim.bank_management_system.exceptions.InvalidStatusException;
import rim.bank_management_system.exceptions.ResourceNotFoundException;
import rim.bank_management_system.repository.BankAccountRepository;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class AccountAuthorizationService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccount getAuthorizedAccount(Long accountId, String username) {
        return authorizeAccount(() -> bankAccountRepository.findById(accountId), username);
    }

    public BankAccount getAuthorizedAccount(String iban, String username) {
        return authorizeAccount(() -> bankAccountRepository.findByIban(iban), username);
    }

    private BankAccount authorizeAccount(Supplier<Optional<BankAccount>> accountSupplier, String username) {
        BankAccount account = accountSupplier.get()
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (!account.getUser().getUsername().equals(username)) {
            throw new AccessDeniedBankingException("Unauthorized access to account");
        }

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidStatusException("Account is not active");
        }

        return account;
    }
}
