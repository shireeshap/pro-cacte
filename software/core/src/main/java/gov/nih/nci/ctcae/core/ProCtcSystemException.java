package gov.nih.nci.ctcae.core;

public class ProCtcSystemException extends RuntimeException {
    private String errorCode;

    public ProCtcSystemException(String code, String message) {
        this(message);
        this.errorCode = code;
    }

    public ProCtcSystemException(String code, String message, Throwable cause) {
        this(message, cause);
        this.errorCode = code;
    }

    public ProCtcSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProCtcSystemException(Throwable cause) {
        super(cause);
    }

    public ProCtcSystemException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }


}
