package rim.bank_management_system.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import rim.bank_management_system.dto.response.AccountResponse;
import rim.bank_management_system.entity.AccountStatus;
import rim.bank_management_system.entity.BankAccount;
import rim.bank_management_system.entity.User;
import rim.bank_management_system.exceptions.AccessDeniedBankingException;
import rim.bank_management_system.exceptions.InvalidStatusException;
import rim.bank_management_system.exceptions.ResourceNotFoundException;
import rim.bank_management_system.repository.BankAccountRepository;
import rim.bank_management_system.repository.UserRepository;
import rim.bank_management_system.service.BankAccountService;
import rim.bank_management_system.util.IbanGeneratorUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final IbanGeneratorUtil ibanGeneratorUtil;
    private final ModelMapper modelMapper;

    @Override
    public AccountResponse createAccount(String username, String pinCode) {
        User user = getUserByUsername(username);
        BankAccount account = initializeNewAccount(user);
        account.setPinCode(pinCode);
        bankAccountRepository.save(account);
        return mapToResponse(account);
    }

    @Override
    public List<AccountResponse> getUserAccounts(String username) {
        return bankAccountRepository.findAllByUserUsername(username).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void requestClosure(Long accountId, String username) {
        BankAccount account = getAccountById(accountId);
        validateOwnership(account, username);
        validateAccountIsActive(account);
        account.setStatus(AccountStatus.PENDING_CLOSE);
        bankAccountRepository.save(account);
    }

    @Override
    public void approveClosure(Long accountId) {
        BankAccount account = getAccountById(accountId);
        validatePendingClosure(account);
        account.setStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        bankAccountRepository.save(account);
    }

    // --- Köməkçi metodlar ---

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private BankAccount initializeNewAccount(User user) {
        return BankAccount.builder()
                .user(user)
                .iban(ibanGeneratorUtil.generateAzerbaijanIban())
                .balance(BigDecimal.valueOf(100))
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private AccountResponse mapToResponse(BankAccount account) {
        return modelMapper.map(account, AccountResponse.class);
    }

    private BankAccount getAccountById(Long accountId) {
        return bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    private void validateOwnership(BankAccount account, String username) {
        if (!account.getUser().getUsername().equals(username)) {
            throw new AccessDeniedBankingException("You don't own this account");
        }
    }

    private void validateAccountIsActive(BankAccount account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidStatusException("Account must be active to request closure");
        }
    }

    private void validatePendingClosure(BankAccount account) {
        if (account.getStatus() != AccountStatus.PENDING_CLOSE) {
            throw new InvalidStatusException("Account isn't pending closure");
        }
    }
}
