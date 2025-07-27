package rim.bank_management_system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rim.bank_management_system.dto.request.DepositRequest;
import rim.bank_management_system.dto.request.WithdrawRequest;
import rim.bank_management_system.dto.response.TransactionResponse;
import rim.bank_management_system.service.ATMService;

@RestController
@RequestMapping("/api/atm")
@Tag(name = "ATM Controller", description = "Handles ATM operations")

@RequiredArgsConstructor
public class ATMController {

    private final ATMService atmService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            Authentication authentication,
            @RequestBody DepositRequest request) {

        String username = authentication.getName();
        TransactionResponse response = atmService.deposit(username, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            Authentication authentication,
            @RequestBody WithdrawRequest request) {

        String username = authentication.getName();
        TransactionResponse response = atmService.withdraw(username, request);
        return ResponseEntity.ok(response);
    }
}

