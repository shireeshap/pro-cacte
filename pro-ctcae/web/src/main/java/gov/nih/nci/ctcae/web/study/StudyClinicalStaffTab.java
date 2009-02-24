package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
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


        if (command.getPrincipalInvestigator().getRole() == null) {
            command.getPrincipalInvestigator().setRole(Role.PI);

        }
        if (command.getOverallDataCoordinator().getRole() == null) {
            command.getOverallDataCoordinator().setRole(Role.ODC);

        }
        if (command.getLeadCRA().getRole() == null) {
            command.getLeadCRA().setRole(Role.LEAD_CRA);

        }
        return referenceData;
    }


    @Override
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {

        command.apply();
        super.postProcess(request, command, errors);


    }

}
