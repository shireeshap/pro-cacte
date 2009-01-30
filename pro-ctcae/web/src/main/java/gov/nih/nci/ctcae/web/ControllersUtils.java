package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.FormController;
import gov.nih.nci.ctcae.web.study.CreateStudyController;
import gov.nih.nci.ctcae.web.study.StudyCommand;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ControllersUtils {


    public static CreateFormCommand getFormCommand(final HttpServletRequest request) {
        return (CreateFormCommand) request.getSession().getAttribute(FormController.class.getName() + ".FORM." + "command");


    }


    public static StudyCommand getStudyCommand(HttpServletRequest request) {
        StudyCommand studyCommand = (StudyCommand)
                request.getSession().getAttribute(CreateStudyController.class.getName() + ".FORM." + "command");
        return studyCommand;
    }
}
