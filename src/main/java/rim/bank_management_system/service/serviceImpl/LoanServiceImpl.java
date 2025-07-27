package rim.bank_management_system.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rim.bank_management_system.dto.request.LoanRequest;
import rim.bank_management_system.dto.response.LoanResponse;
import rim.bank_management_system.entity.*;
import rim.bank_management_system.entity.embedded.LoanApplicationDetails;
import rim.bank_management_system.exceptions.AccessDeniedBankingException;
import rim.bank_management_system.exceptions.InvalidStatusException;
import rim.bank_management_system.exceptions.ResourceNotFoundException;
import rim.bank_management_system.repository.BankAccountRepository;
import rim.bank_management_system.repository.LoanRepository;
import rim.bank_management_system.repository.TransactionRepository;
import rim.bank_management_system.service.LoanService;
import rim.bank_management_system.service.helper.AccountAuthorizationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ModelMapper modelMapper;
    private final TransactionRepository transactionRepository;
    private final AccountAuthorizationService accountAuthorizationService;


    @Value("${system.bank.account-id}")
    private Long systemBankAccountId;

    @Override
    @Transactional
    public LoanResponse applyForLoan(LoanRequest request, String username) {
        BankAccount account = accountAuthorizationService.getAuthorizedAccount(request.getAccountId(), username);
        LoanApplicationDetails details= buildLoanApplicationDetails(request);
        BigDecimal monthlyInstallment = calculateMonthlyInstallment(request);

        Loan loan= buildLoanEntity(request,account,details);
        loan.setStatus(determineLoanStatus(details,monthlyInstallment, request.getTermInMonths(), account));

        loanRepository.save(loan);
        return mapToLoanResponse(loan);
    }

    @Override
    public LoanResponse getLoanById(Long id, String username) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if (!loan.getAccount().getUser().getUsername().equals(username)) {
            throw new AccessDeniedBankingException("Unauthorized to view this loan");
        }

        return mapToLoanResponse(loan);
    }

    @Override
    public List<LoanResponse> getLoansByUsername(String username) {
        List<Loan> loans = loanRepository.findByAccountUserUsername(username);
        return loans.stream()
                .map(this::mapToLoanResponse)
                .toList();
    }

    @Override
    @Transactional
    public LoanResponse approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidStatusException("Only pending loans can be approved");
        }

        loan.setStatus(LoanStatus.APPROVED);

        BankAccount account = loan.getAccount();
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(loan.getAmount());
        account.setBalance(newBalance);
        BankAccount bankAccount = bankAccountRepository.findById(systemBankAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Transaction transaction = Transaction.builder()
                .fromAccount(bankAccount)
                .toAccount(account)
                .toIban(account.getIban())
                .amount(loan.getAmount())
                .type(TransactionType.LOAN_DISBURSEMENT)
                .timestamp(LocalDateTime.now())
                .successful(true)
                .build();
        transactionRepository.save(transaction);

        loanRepository.save(loan);
        bankAccountRepository.save(account);

        return mapToLoanResponse(loan);
    }

    @Override
    public LoanResponse rejectLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidStatusException("Only pending loans can be rejected");
        }

        loan.setStatus(LoanStatus.REJECTED);
        loanRepository.save(loan);
        return mapToLoanResponse(loan);
    }
    
    // --- Köməkçi metodlar ---

    private LoanApplicationDetails buildLoanApplicationDetails( LoanRequest request) {
        return LoanApplicationDetails.builder()
                .monthlyIncome(request.getMonthlySalary())
                .age(request.getAge())
                .creditScore(request.getCreditScore())
                .hasGuarantor(request.isHasGuarantor())
                .build();
    }

    private BigDecimal calculateMonthlyInstallment(LoanRequest request) {
        BigDecimal principal = request.getAmount();
        BigDecimal annualRate = request.getInterestRate(); // misal: 0.15 (15%)
        int termInMonths = request.getTermInMonths();

        // Aylıq faiz dərəcəsi = illik faiz / 12
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        // (1 + r)^n
        BigDecimal onePlusRateToPower = monthlyRate.add(BigDecimal.ONE)
                .pow(termInMonths);
        // pay = r * (1 + r)^n
        BigDecimal numerator = monthlyRate.multiply(onePlusRateToPower);
        // bölən = (1 + r)^n - 1
        BigDecimal denominator = onePlusRateToPower.subtract(BigDecimal.ONE);
        BigDecimal annuityFactor = numerator.divide(denominator, 10, RoundingMode.HALF_UP);
        // aylıq ödəniş = əsas məbləğ * annuitet əmsalı
        BigDecimal monthlyInstallment = principal.multiply(annuityFactor);

        return monthlyInstallment.setScale(2, RoundingMode.HALF_UP);
    }

    private Loan buildLoanEntity(LoanRequest request, BankAccount account,LoanApplicationDetails details){
        return Loan.builder()
                .amount(request.getAmount())
                .interestRate(request.getInterestRate())
                .termInMonths(request.getTermInMonths())
                .applicationDetails(details)
                .account(account)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(request.getTermInMonths()))
                .build();
    }

    private LoanStatus determineLoanStatus(LoanApplicationDetails details,BigDecimal monthlyInstallment,
                                           int term, BankAccount account) {
        boolean salaryCheck= monthlyInstallment.compareTo(details.getMonthlyIncome().multiply(BigDecimal.valueOf(0.5)))<=0;
        boolean isCreditScoreOk= details.getCreditScore() >=600;
        boolean isAgeOk= details.getAge()>=18 && details.getAge()<=65;
        boolean isAgeAtEndOk=details.getAge()+term/12<=65;
        boolean hasExistingLoan=loanRepository.existsByAccountIdAndStatus(account.getId(),LoanStatus.APPROVED);

        boolean isEligible= salaryCheck && isCreditScoreOk && isAgeOk && isAgeAtEndOk &&
                (!hasExistingLoan || details.isHasGuarantor());

        return isEligible ? LoanStatus.PENDING : LoanStatus.REJECTED;
    }

    private LoanResponse mapToLoanResponse(Loan loan){
        return modelMapper.map(loan, LoanResponse.class);
    }
}
