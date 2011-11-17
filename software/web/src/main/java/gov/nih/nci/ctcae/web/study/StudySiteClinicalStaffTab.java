package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @since Feb 11, 2009
 */
public class StudySiteClinicalStaffTab extends SecuredTab<StudyCommand> {

    private StudyRepository studyRepository;

    private StudyOrganizationRepository studyOrganizationRepository;
    private UserRepository userRepository;

    public StudySiteClinicalStaffTab() {
        super("study.tab.study_site_clinical_staff", "study.tab.study_site_clinical_staff", "study/study_site_clinical_staff");
    }


    @Override
    public void onDisplay(HttpServletRequest request, StudyCommand command) {
        super.onDisplay(request, command);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.isAdmin()) {
            ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
            command.setDefaultOrganization(clinicalStaff.getOrganizationClinicalStaffs().get(0).getOrganization());
        }

    }


    @Override
    public Map<String, Object> referenceData(StudyCommand command) {
        Map<String, Object> referenceData = super.referenceData(command);

        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByStudyId(command.getStudy().getId());
        query.filterByStudySiteAndLeadSiteOnly();

        List<StudyOrganization> studySites = (List<StudyOrganization>) studyOrganizationRepository.find(query);
        if (!studySites.isEmpty() && command.getSelectedStudySite() == null) {
            command.setSelectedStudySite((StudySite) studySites.iterator().next());
            for (StudyOrganization studySite : studySites) {
                if (studySite.getOrganization().equals(command.getDefaultOrganization())) {
                    command.setSelectedStudySite((StudySite) studySite);
                }
            }
        }
        referenceData.put("studySites", studySites);
        referenceData.put("roleStatusOptions", ListValues.getRoleStatusType());
        referenceData.put("leadCRA", command.getStudy().getStudyOrganizationClinicalStaffByRole(Role.LEAD_CRA));
        referenceData.put("OverallPI", command.getStudy().getStudyOrganizationClinicalStaffByRole(Role.PI));     
        referenceData.put("notifyOptions", ListValues.getNotificationRequired());

        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        studyRepository.addStudyOrganizationClinicalStaff(command.getStudyOrganizationClinicalStaffs());

    }

    public String getRequiredPrivilege() {
        //return Privilege.PRIVILEGE_ADD_STUDY_SITE_CLINICAL_STAFF;
    	//return Privilege.PRIVILEGE_CREATE_CLINICAL_STAFF;
    	return Privilege.PRIVILEGE_ADD_STUDY_SITE_STAFF;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}