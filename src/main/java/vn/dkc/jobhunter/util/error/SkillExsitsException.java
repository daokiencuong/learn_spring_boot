package vn.dkc.jobhunter.util.error;

public class SkillExsitsException extends RuntimeException{
    public SkillExsitsException(String message) {
        super(message);
    }

    public SkillExsitsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkillExsitsException(Throwable cause) {
        super(cause);
    }

    public SkillExsitsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
