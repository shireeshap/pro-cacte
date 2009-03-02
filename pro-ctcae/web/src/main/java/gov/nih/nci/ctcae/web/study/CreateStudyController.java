package gov.nih.nci.ctcae.web.study;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

//
/**
 * The Class StudyController.
 *
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class CreateStudyController extends StudyController {

    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {

        StudyCommand studyCommand = new StudyCommand();
        return studyCommand;
    }


}