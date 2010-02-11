package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import org.springframework.security.context.SecurityContextHolder;

//
/**
 * The Class StudyController.
 *
 * @author Vinay Kumar
 * @since Oct 17, 2008
 */
public class CreateStudyController extends StudyController {

    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {
        StudyCommand studyCommand = new StudyCommand();
        return studyCommand;
    }
}