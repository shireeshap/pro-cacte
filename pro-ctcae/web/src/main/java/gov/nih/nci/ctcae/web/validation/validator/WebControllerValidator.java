package gov.nih.nci.ctcae.web.validation.validator;

import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;

//
/**
 * The Interface WebControllerValidator.
 *
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public interface WebControllerValidator {

    /**
     * Validate the domain object after. This method should be called in
     * {@link org.springframework.web.servlet.mvc.BaseCommandController#onBind} method to validate the form submission. If there are any
     * validation issues found, this method will update the errors object.
     *
     * @param request the request
     * @param command the command object
     * @param errors  the errors
     */
    public void validate(final HttpServletRequest request, final Object command,
                         final BindException errors);

}
