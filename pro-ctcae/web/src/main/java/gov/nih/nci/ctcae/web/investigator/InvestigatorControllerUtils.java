package gov.nih.nci.ctcae.web.investigator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mehul Gulati
 * Date: Oct 30, 2008
 */
public class InvestigatorControllerUtils {
    public static InvestigatorCommand getInvestigatorCommand(HttpServletRequest request ){
            InvestigatorCommand investigatorCommand = (InvestigatorCommand)
                   request.getSession().getAttribute(CreateInvestigatorController.class.getName() + ".FORM." + "investigatorCommand");
        return investigatorCommand;
            }
}
