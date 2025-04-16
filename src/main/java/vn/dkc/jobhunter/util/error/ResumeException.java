package vn.dkc.jobhunter.util.error;

public class ResumeException extends RuntimeException {
    public ResumeException(String message) {
        super(message);
    }

    public ResumeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResumeException(Throwable cause) {
        super(cause);
    }
}
