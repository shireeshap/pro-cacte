package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Feb 11, 2009
 */
public class StudyClinicalStaffTab extends Tab<StudyCommand> {
    public StudyClinicalStaffTab() {
        super("study.tab.clinical_staff", "study.tab.clinical_staff", "study/study_clinical_staff");
    }


    @Override
    public void onDisplay(HttpServletRequest request, StudyCommand command) {
        super.onDisplay(request, command);

        Study study = command.getStudy();
        StudyOrganizationClinicalStaff overallDataCoordinator = new StudyOrganizationClinicalStaff();
        overallDataCoordinator.setRole(Role.ODC);

        study.setOverallDataCoordinator(overallDataCoordinator);

        StudyOrganizationClinicalStaff principalInvestigator = new StudyOrganizationClinicalStaff();
        principalInvestigator.setRole(Role.PI);
        study.setPrincipalInvestigator(principalInvestigator);


        StudyOrganizationClinicalStaff leadCRA = new StudyOrganizationClinicalStaff();
        leadCRA.setRole(Role.LEAD_CRA);
        study.setLeadCRA(leadCRA);


    }

    @Override
    public Map<String, Object> referenceData(StudyCommand command) {
        Map<String, Object> referenceData = super.referenceData(command);

        Study study = command.getStudy();
        List<StudySite> studySites = study.getStudySites();

        List<ListValues> studySiteListValues = new ArrayList<ListValues>();


        for (StudySite studySite : studySites) {
            studySiteListValues.add(new ListValues(String.valueOf(studySite.getId()), studySite.getOrganization().getDisplayName()));
        }
        studySiteListValues.add(new ListValues(String.valueOf(study.getStudyCoordinatingCenter().getId()), study.getStudyCoordinatingCenter().getOrganization().getDisplayName()));

        referenceData.put("studySitesAndCoordinatingCenter", studySiteListValues);


        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
        super.postProcess(request, command, errors);


        Study study = command.getStudy();

        StudyOrganizationClinicalStaff principalInvestigator = study.getPrincipalInvestigator();

        principalInvestigator.getStudyOrganization().addOrUpdateStudyOrganizationClinicalStaff(principalInvestigator);

        study.getStudyCoordinatingCenter().addOrUpdateStudyOrganizationClinicalStaff(study.getOverallDataCoordinator());

        StudyOrganizationClinicalStaff leadCRA = study.getLeadCRA();
        leadCRA.getStudyOrganization().addOrUpdateStudyOrganizationClinicalStaff(leadCRA);

    }

}
