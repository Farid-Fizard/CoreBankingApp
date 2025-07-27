package rim.bank_management_system.service;

import rim.bank_management_system.dto.response.AccountResponse;

import java.util.List;

public interface BankAccountService {
    AccountResponse createAccount(String username, String pinCode);
    List<AccountResponse> getUserAccounts(String username);
    void requestClosure(Long accountId, String username);
    void approveClosure(Long accountId);
}
