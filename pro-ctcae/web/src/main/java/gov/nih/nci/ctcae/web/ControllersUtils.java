package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.FormController;
import gov.nih.nci.ctcae.web.study.CreateStudyController;
import gov.nih.nci.ctcae.web.study.StudyCommand;

import javax.servlet.http.HttpServletRequest;

//
/**
 * The Class ControllersUtils.
 *
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ControllersUtils {


    /**
     * Gets the form command.
     *
     * @param request the request
     * @return the form command
     */
    public static CreateFormCommand getFormCommand(final HttpServletRequest request) {
        return (CreateFormCommand) request.getSession().getAttribute(FormController.class.getName() + ".FORM." + "command");


    }


    /**
     * Gets the study command.
     *
     * @param request the request
     * @return the study command
     */
    public static StudyCommand getStudyCommand(HttpServletRequest request) {
        StudyCommand studyCommand = (StudyCommand)
                request.getSession().getAttribute(CreateStudyController.class.getName() + ".FORM." + "command");
        return studyCommand;
    }
}
