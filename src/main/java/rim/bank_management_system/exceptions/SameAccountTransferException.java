package rim.bank_management_system.exceptions;

public class SameAccountTransferException extends RuntimeException {
    public SameAccountTransferException(String message) {
        super(message);
    }
}
