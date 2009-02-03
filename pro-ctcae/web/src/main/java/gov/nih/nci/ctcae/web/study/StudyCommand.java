package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyCoordinatingCenter;
import gov.nih.nci.ctcae.core.domain.StudyFundingSponsor;
import gov.nih.nci.ctcae.core.domain.StudySite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//
/**
 * The Class StudyCommand.
 *
 * @author Vinay Kumar
 * @crated Oct 27, 2008
 */
public class StudyCommand {

    /**
     * The study.
     */
    private Study study;

    /**
     * The objects ids to remove.
     */
    private String objectsIdsToRemove;


    /**
     * Instantiates a new study command.
     */
    public StudyCommand() {
        super();
        this.study = new Study();
        study.setStudyFundingSponsor(new StudyFundingSponsor());
        study.setStudyCoordinatingCenter(new StudyCoordinatingCenter());

    }


    /**
     * Sets the study.
     *
     * @param study the new study
     */
    public void setStudy(Study study) {
        this.study = study;
    }

    /**
     * Gets the objects ids to remove.
     *
     * @return the objects ids to remove
     */
    public String getObjectsIdsToRemove() {
        return objectsIdsToRemove;
    }

    /**
     * Sets the objects ids to remove.
     *
     * @param objectsIdsToRemove the new objects ids to remove
     */
    public void setObjectsIdsToRemove(String objectsIdsToRemove) {
        this.objectsIdsToRemove = objectsIdsToRemove;
    }

    /**
     * Gets the study.
     *
     * @return the study
     */
    public Study getStudy() {
        return study;
    }

    /**
     * Removes the study sites.
     */
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
