package gov.nih.nci.ctcae.web.form;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class FormControllersUtils {

    public static CreateFormCommand getFormCommand(final HttpServletRequest request) {
        CreateFormCommand createFormCommand = (CreateFormCommand)
                request.getSession().getAttribute(CreateFormController.class.getName() + ".FORM." + "createFormCommand");
        return createFormCommand;

    }

}
