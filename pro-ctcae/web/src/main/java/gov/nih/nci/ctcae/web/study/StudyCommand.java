package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.*;

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

    private StudyOrganizationClinicalStaff overallDataCoordinator, leadCRA, principalInvestigator;


    /**
     * Instantiates a new study command.
     */
    public StudyCommand() {
        super();
        this.study = new Study();
        study.setStudyFundingSponsor(new StudyFundingSponsor());
        study.setStudyCoordinatingCenter(new StudyCoordinatingCenter());
        overallDataCoordinator = new StudyOrganizationClinicalStaff();

        leadCRA = new StudyOrganizationClinicalStaff();
        principalInvestigator = new StudyOrganizationClinicalStaff();
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

        getStudy().getStudyCoordinatingCenter().addOrUpdateStudyOrganizationClinicalStaff(getOverallDataCoordinator());


        StudyOrganization studyOrganization = study.getStudyOrganization(getLeadCRA());
        studyOrganization.addOrUpdateStudyOrganizationClinicalStaff(getLeadCRA());

        StudyOrganization anotherStudyOrganization = study.getStudyOrganization(getPrincipalInvestigator());
        anotherStudyOrganization.addOrUpdateStudyOrganizationClinicalStaff(getPrincipalInvestigator());

    }

    public void setOverallDataCoordinator(StudyOrganizationClinicalStaff overallDataCoordinator) {
        this.overallDataCoordinator = overallDataCoordinator;
    }

    public void setLeadCRA(StudyOrganizationClinicalStaff leadCRA) {
        this.leadCRA = leadCRA;
    }

    public void setPrincipalInvestigator(StudyOrganizationClinicalStaff principalInvestigator) {
        this.principalInvestigator = principalInvestigator;
    }

    public StudyOrganizationClinicalStaff getOverallDataCoordinator() {
        return overallDataCoordinator;
    }

    public StudyOrganizationClinicalStaff getLeadCRA() {
        return leadCRA;
    }

    public StudyOrganizationClinicalStaff getPrincipalInvestigator() {
        return principalInvestigator;
    }

}
