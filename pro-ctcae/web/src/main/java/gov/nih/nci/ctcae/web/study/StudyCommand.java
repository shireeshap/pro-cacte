package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyFundingSponsor;
import gov.nih.nci.ctcae.core.domain.StudyCoordinatingCenter;
import gov.nih.nci.ctcae.core.domain.StudySite;
import org.apache.commons.lang.StringUtils;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class StudyCommand {

    private Study study;

    private String objectsIdsToRemove;


    public StudyCommand() {
        this.study = new Study();
        study.setStudyFundingSponsor(new StudyFundingSponsor());
        study.setStudyCoordinatingCenter(new StudyCoordinatingCenter());

    }

    public StudyCommand(Study study) {
        this.study = study;
    }


    public void setStudy(Study study) {
        this.study = study;
    }

    public String getObjectsIdsToRemove() {
        return objectsIdsToRemove;
    }

    public void setObjectsIdsToRemove(String objectsIdsToRemove) {
        this.objectsIdsToRemove = objectsIdsToRemove;
    }

    public Study getStudy() {
        return study;
    }

    public void removeStudySites() {
        Set<String> indexes = org.springframework.util.StringUtils.commaDelimitedListToSet(objectsIdsToRemove);
        List<StudySite> studySitesToRemove = new ArrayList<StudySite>();
        for (String index : indexes) {
            StudySite studySite = study.getStudySites().get(Integer.parseInt(index));
            studySitesToRemove.add(studySite);
        }

        for (StudySite studySite : studySitesToRemove) {
            study.removeStudySite(studySite);
        }
    }
}
