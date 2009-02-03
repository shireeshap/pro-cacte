package gov.nih.nci.ctcae.core.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class CtcAeSystemException.
 *
 * @author Vinay Kumar
 */
public class CtcAeSystemException extends RuntimeException {

    /**
     * Instantiates a new ctc ae system exception.
     *
     * @param message the message
     */
    public CtcAeSystemException(final String message) {
        super(message);

    }


    /**
     * Instantiates a new ctc ae system exception.
     *
     * @param s the s
     * @param e the e
     */
    public CtcAeSystemException(String s, Throwable e) {
        super(s, e);

    }

    public CtcAeSystemException(Exception e) {
        super(e);

    }
}
