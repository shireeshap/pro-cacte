package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.StudySite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Feb 11, 2009
 */
public class StudyInvestigatorsTab extends Tab<StudyCommand> {
    public StudyInvestigatorsTab() {
        super("study.tab.study_investigator", "study.tab.study_investigator", "study/study_investigators");
    }

    @Override
    public Map<String, Object> referenceData(StudyCommand command) {
        Map<String, Object> referenceData = super.referenceData(command);

        List<StudySite> studySites = command.getStudy().getStudySites();
        List<Organization> organizations = new ArrayList<Organization>();
        for (StudySite studySite : studySites) {
            organizations.add(studySite.getOrganization());
        }
        referenceData.put("sites", organizations);
        
        return referenceData;
    }


}
