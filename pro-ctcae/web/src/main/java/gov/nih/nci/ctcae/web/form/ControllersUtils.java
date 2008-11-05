package gov.nih.nci.ctcae.web.form;

import org.springframework.web.servlet.mvc.BaseCommandController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ControllersUtils {

    public static Object getFormCommand(final HttpServletRequest request, BaseCommandController baseCommandController) {
        Object command = request.getSession().getAttribute(baseCommandController.getClass().getName() + ".FORM." + baseCommandController.getCommandName());
        return command;

    }


}
