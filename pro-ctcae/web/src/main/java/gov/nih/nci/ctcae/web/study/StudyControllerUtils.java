package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.form.CreateFormController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class StudyControllerUtils {
    public static Study getStudyCommand(HttpServletRequest request) {
        Study study = (Study)
                request.getSession().getAttribute(CreateStudyController.class.getName() + ".FORM." + "studyCommand");
        return study;


    }
}
