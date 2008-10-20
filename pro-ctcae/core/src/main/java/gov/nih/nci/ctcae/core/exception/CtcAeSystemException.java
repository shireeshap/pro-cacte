package gov.nih.nci.ctcae.core.exception;

import javax.mail.MessagingException;

/**
 * @author
 */
public class CtcAeSystemException extends Throwable {

    public CtcAeSystemException(final String message) {
        super(message);

    }

    public CtcAeSystemException(final String s, final MessagingException e) {
        super(s, e);
    }
}
