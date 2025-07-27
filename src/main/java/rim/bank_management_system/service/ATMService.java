package rim.bank_management_system.service;

import rim.bank_management_system.dto.request.DepositRequest;
import rim.bank_management_system.dto.request.WithdrawRequest;
import rim.bank_management_system.dto.response.TransactionResponse;

public interface ATMService {
    TransactionResponse deposit(String username, DepositRequest request);
    TransactionResponse withdraw(String username, WithdrawRequest request);
}


