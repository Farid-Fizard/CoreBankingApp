package rim.bank_management_system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rim.bank_management_system.dto.request.AccountRequest;
import rim.bank_management_system.dto.response.AccountResponse;
import rim.bank_management_system.service.BankAccountService;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Bank account Controller", description = "Handles account operations")

@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(Authentication authentication,
    @RequestBody AccountRequest request) {
        String username = authentication.getName();
        AccountResponse response = accountService.createAccount(username, request.getPinCode());
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getUserAccounts(Authentication authentication) {
        String username = authentication.getName();
        List<AccountResponse> responseList = accountService.getUserAccounts(username);
        return ResponseEntity.ok(responseList);
    }


    @PostMapping("/{id}/request-closure")
    public ResponseEntity<Void> requestClosure(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        accountService.requestClosure(id, username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/approve-closure")
    public ResponseEntity<Void> approveClosure(@PathVariable Long id) {
        accountService.approveClosure(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
