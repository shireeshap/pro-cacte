package gov.nih.nci.ctcae.core.exception;

//
/**
 * The Class CtcAeSystemException.
 *
 * @author Vinay Kumar
 */
public class UsernameAlreadyExistsException extends RuntimeException {

    /**
     * Instantiates a new ctc ae system exception.
     *
     * @param username the message
     */
    public UsernameAlreadyExistsException(final String username) {
        super("Username "+ username + " already exists. Please use a different UserName");

    }

}