package gov.nih.nci.ctcae.web.form;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class FormControllersUtils {

    public static CreateFormCommand getFormCommand(final HttpServletRequest request) {
        CreateFormCommand createFormCommand = getCommand(request, CreateFormController.class.getName(), "createFormCommand");
        return createFormCommand;

    }

    public static CreateFormCommand getCommand(HttpServletRequest request, String className, String commandName) {
        CreateFormCommand createFormCommand = (CreateFormCommand)
                request.getSession().getAttribute(className + ".FORM." + commandName);
        return createFormCommand;
    }

}
