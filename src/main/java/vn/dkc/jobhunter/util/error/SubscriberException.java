package vn.dkc.jobhunter.util.error;

public class SubscriberException extends RuntimeException {
    public SubscriberException(String message) {
        super(message);
    }

    public SubscriberException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubscriberException(Throwable cause) {
        super(cause);
    }
}
