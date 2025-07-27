package rim.bank_management_system.exceptions;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException(String message) {
        super(message);
    }
}
