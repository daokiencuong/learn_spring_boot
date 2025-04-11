package vn.dkc.jobhunter.util.error;

public class EmailExsitsException extends RuntimeException{
    public EmailExsitsException(String message) {
        super(message);
    }
}
