package rim.bank_management_system.service;

import rim.bank_management_system.dto.request.ExternalTransferRequest;
import rim.bank_management_system.dto.request.InternalTransferRequest;
import rim.bank_management_system.dto.response.TransactionResponse;

public interface TransactionService {
    TransactionResponse transferBetweenOwnAccounts(String username, InternalTransferRequest request);
    TransactionResponse transferToOtherUser(String username, ExternalTransferRequest request);
}
