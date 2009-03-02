package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Feb 11, 2009
 */
public class StudySiteClinicalStaffTab extends SecuredTab<StudyCommand> {
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

        Study study = command.getStudy();
        List<StudySite> studySites = study.getStudySites();


        referenceData.put("studySites", studySites);
        referenceData.put("roleStatusOptions", ListValues.getRoleStatusType());


        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {

        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : command.getStudyOrganizationClinicalStaffs()) {
            studyOrganizationClinicalStaff.getStudyOrganization().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        }

    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_ADD_STUDY_SITE_CLINICAL_STAFF;

    }
}