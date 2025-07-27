package rim.bank_management_system.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("ResourceNotFoundException: {}", ex.getMessage());
        ApiError error = new ApiError("NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedBankingException.class)
    public ResponseEntity<ApiError> handleAccessDeniedBankingException(AccessDeniedBankingException ex) {
        ApiError error = new ApiError("ACCESS_DENIED", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ApiError> handleInvalidStatusException(InvalidStatusException ex) {
        ApiError error = new ApiError("INVALID_STATUS", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<ApiError> handleDuplicatedUserException(DuplicatedUserException ex) {
        log.warn("DuplicatedUserException: {}", ex.getMessage());
        ApiError error = new ApiError("DUPLICATED", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiError> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        ApiError error = new ApiError("INSUFFICIENT_BALANCE", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SameAccountTransferException.class)
    public ResponseEntity<ApiError> handleSameAccountTransferException(SameAccountTransferException ex) {
        ApiError error = new ApiError("SAME_ACCOUNT_TRANSFER", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
