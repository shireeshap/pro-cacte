package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.ArrayList;
import java.util.List;

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

    private Integer selectedStudySiteId;

    private StudyOrganizationClinicalStaff leadCRA;

    private StudyOrganizationClinicalStaff overallDataCoordinator;

    private StudyOrganizationClinicalStaff principalInvestigator;

    private List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = new ArrayList<StudyOrganizationClinicalStaff>();

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

    public StudyOrganizationClinicalStaff getOverallDataCoordinator() {
        if (overallDataCoordinator == null) {
            overallDataCoordinator = study.getOverallDataCoordinator();

        }

        return overallDataCoordinator;
    }

    public void setOverallDataCoordinator(StudyOrganizationClinicalStaff overallDataCoordinator) {
        this.overallDataCoordinator = overallDataCoordinator;

    }

    public StudyOrganizationClinicalStaff getPrincipalInvestigator() {

        if (principalInvestigator == null) {
            principalInvestigator = study.getPrincipalInvestigator();

        }
        return principalInvestigator;
    }

    public void setPrincipalInvestigator(StudyOrganizationClinicalStaff principalInvestigator) {
        this.principalInvestigator = principalInvestigator;

    }

    public StudyOrganizationClinicalStaff getLeadCRA() {

        if (leadCRA == null) {
            leadCRA = study.getLeadCRA();

        }
        return leadCRA;
    }


    public void setLeadCRA(StudyOrganizationClinicalStaff leadCRA) {
        this.leadCRA = leadCRA;

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


    public void updateClinicalStaffs() {

        setLeadCRA(study.getLeadCRA());
        setPrincipalInvestigator(study.getPrincipalInvestigator());
        setOverallDataCoordinator(study.getOverallDataCoordinator());
        setStudyOrganizationClinicalStaffs(study.getStudyOrganizationClinicalStaffs());

    }

    public List<StudyOrganizationClinicalStaff> getStudyOrganizationClinicalStaffs() {
        if (studyOrganizationClinicalStaffs.isEmpty()) {
            studyOrganizationClinicalStaffs = study.getStudyOrganizationClinicalStaffs();
        }
        return studyOrganizationClinicalStaffs;
    }

    public void addStudyOrganizationClinicalStaff(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        getStudyOrganizationClinicalStaffs().add(studyOrganizationClinicalStaff);
    }

    public void setStudyOrganizationClinicalStaffs(List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs) {
        this.studyOrganizationClinicalStaffs = studyOrganizationClinicalStaffs;
    }

    public Integer getSelectedStudySiteId() {
        List<StudySite> studySites = study.getStudySites();
        if (!studySites.isEmpty() && selectedStudySiteId == null) {
            selectedStudySiteId = studySites.get(0).getId();
        }
        return selectedStudySiteId;
    }

    public void setSelectedStudySiteId(Integer selectedStudySiteId) {
        this.selectedStudySiteId = selectedStudySiteId;
    }
}
