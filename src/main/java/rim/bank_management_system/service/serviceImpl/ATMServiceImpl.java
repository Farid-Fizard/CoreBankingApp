package rim.bank_management_system.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import rim.bank_management_system.dto.request.DepositRequest;
import rim.bank_management_system.dto.request.WithdrawRequest;
import rim.bank_management_system.dto.response.TransactionResponse;
import rim.bank_management_system.entity.BankAccount;
import rim.bank_management_system.entity.Transaction;
import rim.bank_management_system.entity.TransactionType;
import rim.bank_management_system.exceptions.AccessDeniedBankingException;
import rim.bank_management_system.exceptions.InsufficientBalanceException;
import rim.bank_management_system.repository.BankAccountRepository;
import rim.bank_management_system.repository.TransactionRepository;
import rim.bank_management_system.service.ATMService;
import rim.bank_management_system.service.helper.AccountAuthorizationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ATMServiceImpl implements ATMService {

    private final BankAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountAuthorizationService authorizationService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionResponse deposit(String username, DepositRequest request) {
        BankAccount toAccount = authorizationService.getAuthorizedAccount(request.getIban(), username);
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        accountRepository.save(toAccount);

        Transaction transaction = buildTransaction(
                null,
                toAccount,
                toAccount.getIban(),
                request.getAmount(),
                TransactionType.DEPOSIT
        );
        transactionRepository.save(transaction);
        return mapToResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(String username, WithdrawRequest request) {
        BankAccount fromAccount = authorizationService.getAuthorizedAccount(request.getIban(), username);

        if (!fromAccount.getPinCode().equals(request.getPinCode())) {
            throw new AccessDeniedBankingException("Invalid PIN code");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        accountRepository.save(fromAccount);

        Transaction transaction = buildTransaction(
                fromAccount,
                null,
                fromAccount.getIban(),
                request.getAmount(),
                TransactionType.WITHDRAWAL
        );

        transactionRepository.save(transaction);
        return mapToResponse(transaction);
    }

    private Transaction buildTransaction(BankAccount from, BankAccount to, String toIban,
                                         BigDecimal amount, TransactionType type) {
        return Transaction.builder()
                .fromAccount(from)
                .toAccount(to)
                .toIban(toIban)
                .amount(amount)
                .type(type)
                .timestamp(LocalDateTime.now())
                .successful(true)
                .build();
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return modelMapper.map(transaction, TransactionResponse.class);
    }
}

