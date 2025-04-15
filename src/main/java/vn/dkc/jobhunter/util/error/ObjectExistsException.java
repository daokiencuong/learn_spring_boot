package vn.dkc.jobhunter.util.error;

public class ObjectExistsException extends RuntimeException {
    public ObjectExistsException(String message) {
        super(message);
    }

    public ObjectExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectExistsException(Throwable cause) {
        super(cause);
    }
}
