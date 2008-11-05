package gov.nih.nci.ctcae.core.exception;

/**
 * @author Vinay Kumar
 */
public class CtcAeSystemException extends RuntimeException {

    public CtcAeSystemException(final String message) {
        super(message);

    }


    public CtcAeSystemException(String s, Throwable e) {
        super(s, e);

    }
}
