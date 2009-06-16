package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @since Feb 11, 2009
 */
public class StudySiteClinicalStaffTab extends SecuredTab<StudyCommand> {

    private StudyRepository studyRepository;

    private StudyOrganizationRepository studyOrganizationRepository;

    public StudySiteClinicalStaffTab() {
        super("study.tab.study_site_clinical_staff", "study.tab.study_site_clinical_staff", "study/study_site_clinical_staff");
    }


    @Override
    public void onDisplay(HttpServletRequest request, StudyCommand command) {
        super.onDisplay(request, command);


    }


    @Override
    public Map<String, Object> referenceData(StudyCommand command) {
        Map<String, Object> referenceData = super.referenceData(command);

        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByStudyId(command.getStudy().getId());
        query.filterByStudySiteAndLeadSiteOnly();

        Collection<StudyOrganization> studySites = studyOrganizationRepository.find(query);
        if (!studySites.isEmpty() && command.getSelectedStudySite() == null) {
            command.setSelectedStudySite((StudySite) studySites.iterator().next());
        }


        referenceData.put("studySites", studySites);
        referenceData.put("roleStatusOptions", ListValues.getRoleStatusType());
        referenceData.put("leadCRA", command.getStudy().getStudyOrganizationClinicalStaffByRole(Role.LEAD_CRA));
        referenceData.put("OverallPI", command.getStudy().getStudyOrganizationClinicalStaffByRole(Role.PI));


        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        studyRepository.addStudyOrganizationClinicalStaff(command.getStudyOrganizationClinicalStaffs());

    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_ADD_STUDY_SITE_CLINICAL_STAFF;

    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

}