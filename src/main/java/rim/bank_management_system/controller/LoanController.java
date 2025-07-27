package rim.bank_management_system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rim.bank_management_system.dto.request.LoanRequest;
import rim.bank_management_system.dto.response.LoanResponse;
import rim.bank_management_system.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loan Controller", description = "Handles loan operations")
@RequiredArgsConstructor
public class LoanController {

    private  final LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<LoanResponse> applyForLoan(@RequestBody LoanRequest request,
                                                     Authentication authentication) {
        String username = authentication.getName();
        LoanResponse response = loanService.applyForLoan(request, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long loanId,
                                                    Authentication authentication) {
        String username = authentication.getName();
        LoanResponse response = loanService.getLoanById(loanId, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myloans")
    public ResponseEntity<List<LoanResponse>> getLoansByUsername(Authentication authentication) {
        String username = authentication.getName();
        List<LoanResponse> responseList = loanService.getLoansByUsername(username);
        return ResponseEntity.ok(responseList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{loanId}/approve")
    public ResponseEntity<LoanResponse> approveLoan(@PathVariable Long loanId) {
        LoanResponse response = loanService.approveLoan(loanId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{loanId}/reject")
    public ResponseEntity<LoanResponse> rejectLoan(@PathVariable Long loanId) {
        LoanResponse response = loanService.rejectLoan(loanId);
        return ResponseEntity.ok(response);
    }

}
