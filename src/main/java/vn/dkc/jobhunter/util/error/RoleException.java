package vn.dkc.jobhunter.util.error;

public class RoleException extends RuntimeException {
    public RoleException(String message) {
        super(message);
    }

    public RoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleException(Throwable cause) {
        super(cause);
    }
}
