package gov.nih.nci.ctcae.web.study;


import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class StudyControllerUtils {
    public static StudyCommand getStudyCommand(HttpServletRequest request) {
        StudyCommand studyCommand = (StudyCommand)
                request.getSession().getAttribute(CreateStudyController.class.getName() + ".FORM." + "command");
        return studyCommand;
    }
}
