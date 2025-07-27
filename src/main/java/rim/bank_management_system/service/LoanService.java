package rim.bank_management_system.service;

import rim.bank_management_system.dto.request.LoanRequest;
import rim.bank_management_system.dto.response.LoanResponse;

import java.util.List;

public interface LoanService {
    LoanResponse applyForLoan(LoanRequest request, String username);
    LoanResponse getLoanById(Long id,String username);
    List<LoanResponse> getLoansByUsername(String username);
    LoanResponse approveLoan(Long loanId);
    LoanResponse rejectLoan(Long loanId);
}
