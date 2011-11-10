package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @since Feb 11, 2009
 */
public class StudyClinicalStaffTab extends SecuredTab<StudyCommand> {
    public StudyClinicalStaffTab() {
        super("study.tab.clinical_staff", "study.tab.clinical_staff", "study/study_clinical_staff");
    }


    @Override
    public void onDisplay(HttpServletRequest request, StudyCommand command) {
        super.onDisplay(request, command);


    }


    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);

        try {
            command.getStudy().getDataCoordinatingCenter().addOrUpdateStudyOrganizationClinicalStaff(command.getOverallDataCoordinator());
        } catch (CtcAeSystemException ex) {
            errors.reject("error", ex.getMessage());
        }

        try {
            command.getStudy().getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(command.getLeadCRA());
        } catch (CtcAeSystemException ex) {
            errors.reject("error", ex.getMessage());
        }

        try {
            command.getStudy().getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(command.getPrincipalInvestigator());
        } catch (CtcAeSystemException ex) {
            errors.reject("error", ex.getMessage());
        }
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_ADD_STUDY_CLINICAL_STAFF;


    }
}
