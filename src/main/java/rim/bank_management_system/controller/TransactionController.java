package rim.bank_management_system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rim.bank_management_system.dto.request.ExternalTransferRequest;
import rim.bank_management_system.dto.request.InternalTransferRequest;
import rim.bank_management_system.dto.response.TransactionResponse;
import rim.bank_management_system.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller", description = "Handles transaction operations")

@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/internal")
    public ResponseEntity<TransactionResponse> transferBetweenOwnAccounts(
            Authentication authentication,
            @RequestBody InternalTransferRequest request
    ) {
        String username = authentication.getName();
        TransactionResponse response = transactionService.transferBetweenOwnAccounts(username, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/external")
    public ResponseEntity<TransactionResponse> transferToOtherUser(
            Authentication authentication,
            @RequestBody ExternalTransferRequest request
    ) {
        String username = authentication.getName();
        TransactionResponse response = transactionService.transferToOtherUser(username, request);
        return ResponseEntity.ok(response);
    }
}
