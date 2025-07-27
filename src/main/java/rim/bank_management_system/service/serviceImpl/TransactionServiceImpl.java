package rim.bank_management_system.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import rim.bank_management_system.dto.request.ExternalTransferRequest;
import rim.bank_management_system.dto.request.InternalTransferRequest;
import rim.bank_management_system.dto.response.TransactionResponse;
import rim.bank_management_system.entity.*;
import rim.bank_management_system.exceptions.*;
import rim.bank_management_system.repository.BankAccountRepository;
import rim.bank_management_system.repository.TransactionRepository;
import rim.bank_management_system.service.TransactionService;
import rim.bank_management_system.service.helper.AccountAuthorizationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final AccountAuthorizationService accountAuthorizationService;

    @Override
    @Transactional
    public TransactionResponse transferBetweenOwnAccounts(String username, InternalTransferRequest request) {
        BankAccount from = accountAuthorizationService.getAuthorizedAccount(request.getFromAccountId(), username);
        BankAccount to = accountAuthorizationService.getAuthorizedAccount(request.getToAccountId(), username);
        return processTransaction(from, to, request.getAmount(), TransactionType.INTERNAL);
    }

    @Override
    @Transactional
    public TransactionResponse transferToOtherUser(String username, ExternalTransferRequest request) {
        BankAccount from = accountAuthorizationService.getAuthorizedAccount(request.getFromAccountId(), username);
        BankAccount to = getAccountByIban(request.getToIban());
        return processTransaction(from, to, request.getAmount(), TransactionType.EXTERNAL);
    }

    // --- Köməkçi metodlar ---

    private TransactionResponse processTransaction(BankAccount from, BankAccount to, BigDecimal amount, TransactionType type) {
        // 1.Accountların doğruluğunu yoxla.
        // 2.Köçürmə üçün balansda lazımi məbləğin olub olmamasını yoxla.
        validateAccounts(from, to, amount);
        // 3.Köçürəndən məbləği çıx, alana əlavə et.
        updateBalances(from, to, amount);
        //4. Tranzaksiyanı yaddaşda saxla
        Transaction transaction = saveTransaction(from, to, amount, type);
        return modelMapper.map(transaction, TransactionResponse.class);
    }

    private void validateAccounts(BankAccount from, BankAccount to, BigDecimal amount) {
        if (from.getId().equals(to.getId())) {
            throw new SameAccountTransferException("Source and target accounts must be different");
        }
        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    private void updateBalances(BankAccount from, BankAccount to, BigDecimal amount) {
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        accountRepository.save(from);
        accountRepository.save(to);
    }

    private Transaction saveTransaction(BankAccount from, BankAccount to, BigDecimal amount, TransactionType type) {
        Transaction transaction = Transaction.builder()
                .fromAccount(from)
                .toAccount(to)
                .toIban(to.getIban())
                .amount(amount)
                .type(type)
                .timestamp(LocalDateTime.now())
                .successful(true)
                .build();
        return transactionRepository.save(transaction);
    }

    public BankAccount getAccountByIban(String iban) {
        return accountRepository.findByIban(iban)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with IBAN: " + iban));
    }
}