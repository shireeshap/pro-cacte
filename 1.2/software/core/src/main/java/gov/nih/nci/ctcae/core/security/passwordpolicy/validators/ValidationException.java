package gov.nih.nci.ctcae.core.security.passwordpolicy.validators;

import gov.nih.nci.ctcae.core.ProCtcSystemException;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Mar 31, 2010
 * Time: 4:11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidationException extends ProCtcSystemException {
    private static final long serialVersionUID = 8625024692592257767L;

    public ValidationException(String message) {
        super(message);
    }

}
