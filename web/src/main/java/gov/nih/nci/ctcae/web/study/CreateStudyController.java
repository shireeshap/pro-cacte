package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.context.SecurityContextHolder;

import java.util.List;

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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
        if (clinicalStaff != null) {
            List<Organization> organizationsWithCCARole = clinicalStaff.getOrganizationsWithCCARole();
            if (organizationsWithCCARole == null || organizationsWithCCARole.size() != 1) {
                throw new CtcAeSystemException("Logged in user is either not a CCA on any organization or is a CCA on multiple organizations.");
            }
            studyCommand.getStudy().getStudySponsor().setOrganization(organizationsWithCCARole.get(0));

        } else {
            throw new CtcAeSystemException("Logged in user is not a valid clinical staff");
        }

        return studyCommand;
    }


}