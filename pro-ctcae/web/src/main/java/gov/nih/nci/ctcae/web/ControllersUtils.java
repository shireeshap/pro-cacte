package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.web.form.AdvanceFormController;
import gov.nih.nci.ctcae.web.form.BasicFormController;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.EditFormController;
import gov.nih.nci.ctcae.web.study.CreateStudyController;
import gov.nih.nci.ctcae.web.study.StudyCommand;
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

    public static CreateFormCommand getFormCommand(final HttpServletRequest request) {
        CreateFormCommand command = (CreateFormCommand) request.getSession().getAttribute(BasicFormController.class.getName() + ".FORM." + "command");
        if (command == null) {
            command = (CreateFormCommand) request.getSession().getAttribute(AdvanceFormController.class.getName() + ".FORM." + "command");
        }
        if (command == null) {
            command = (CreateFormCommand) request.getSession().getAttribute(EditFormController.class.getName() + ".FORM." + "command");
        }
        return command;

    }


    public static StudyCommand getStudyCommand(HttpServletRequest request) {
        StudyCommand studyCommand = (StudyCommand)
                request.getSession().getAttribute(CreateStudyController.class.getName() + ".FORM." + "command");
        return studyCommand;
    }
}
