package rim.bank_management_system.exceptions;

public class AccessDeniedBankingException extends RuntimeException {
    public AccessDeniedBankingException(String message) {
        super(message);
    }
}
