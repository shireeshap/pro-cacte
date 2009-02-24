package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyCoordinatingCenter;
import gov.nih.nci.ctcae.core.domain.StudyFundingSponsor;

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

    private StudyClinicalStaff overallDataCoordinator, leadCRA, principalInvestigator;


    /**
     * Instantiates a new study command.
     */
    public StudyCommand() {
        super();
        this.study = new Study();
        study.setStudyFundingSponsor(new StudyFundingSponsor());
        study.setStudyCoordinatingCenter(new StudyCoordinatingCenter());
        overallDataCoordinator = new StudyClinicalStaff();

        leadCRA = new StudyClinicalStaff();
        principalInvestigator = new StudyClinicalStaff();
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
     * Gets the study.
     *
     * @return the study
     */
    public Study getStudy() {
        return study;
    }

//    /**
//     * Removes the study sites.
//     */
//    public void removeStudySites() {
//        Set<String> indexes = org.springframework.util.StringUtils.commaDelimitedListToSet(objectsIdsToRemove);
//        List<StudySite> studySitesToRemove = new ArrayList<StudySite>();
//        for (String index : indexes) {
//            StudySite studySite = study.getStudySites().get(Integer.parseInt(index));
//            studySitesToRemove.add(studySite);
//        }
//
//        for (StudySite studySite : studySitesToRemove) {
//            study.removeStudySite(studySite);
//        }
//    }

    public void apply() {

        getStudy().addStudyClinicalStaff(getOverallDataCoordinator());
        getStudy().addStudyClinicalStaff(getLeadCRA());
        getStudy().addStudyClinicalStaff(getPrincipalInvestigator());


    }

    public void setOverallDataCoordinator(StudyClinicalStaff overallDataCoordinator) {
        this.overallDataCoordinator = overallDataCoordinator;
    }

    public void setLeadCRA(StudyClinicalStaff leadCRA) {
        this.leadCRA = leadCRA;
    }

    public void setPrincipalInvestigator(StudyClinicalStaff principalInvestigator) {
        this.principalInvestigator = principalInvestigator;
    }

    public StudyClinicalStaff getOverallDataCoordinator() {
        return overallDataCoordinator;
    }

    public StudyClinicalStaff getLeadCRA() {
        return leadCRA;
    }

    public StudyClinicalStaff getPrincipalInvestigator() {
        return principalInvestigator;
    }

}
