package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class StudyCommand.
 *
 * @author Vinay Kumar
 * @since Oct 27, 2008
 */
public class StudyCommand {

    /**
     * The study.
     */
    private Study study;

    private StudySite selectedStudySite;

    private StudyOrganizationClinicalStaff leadCRA;

    private StudyOrganizationClinicalStaff overallDataCoordinator;

    private StudyOrganizationClinicalStaff principalInvestigator;

    private List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = new ArrayList<StudyOrganizationClinicalStaff>();

    private String armIndexToRemove;
    private boolean activeDefaultArm = false;
    private Organization defaultOrganization;

    /**
     * Instantiates a new study command.
     */
    public StudyCommand() {
        super();
        this.study = new Study();
        study.setStudySponsor(new StudySponsor());
        study.setDataCoordinatingCenter(new DataCoordinatingCenter());
        study.setFundingSponsor(new FundingSponsor());
        LeadStudySite leadStudySite = new LeadStudySite();
        study.setLeadStudySite(leadStudySite);


    }

    public StudyCommand(Study study) {
        this.study = study;
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
        setStudyOrganizationClinicalStaffs(study.getStudySiteLevelStudyOrganizationClinicalStaffs());

    }

    public List<StudyOrganizationClinicalStaff> getStudyOrganizationClinicalStaffs() {
        if (studyOrganizationClinicalStaffs.isEmpty()) {
            studyOrganizationClinicalStaffs = study.getStudySiteLevelStudyOrganizationClinicalStaffs();
        }
        return studyOrganizationClinicalStaffs;
    }

    public void addStudyOrganizationClinicalStaff(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        getStudyOrganizationClinicalStaffs().add(studyOrganizationClinicalStaff);
    }

    public void setStudyOrganizationClinicalStaffs(List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs) {
        this.studyOrganizationClinicalStaffs = studyOrganizationClinicalStaffs;
    }


    public StudySite getSelectedStudySite() {
        return selectedStudySite;
    }

    public void setSelectedStudySite(StudySite selectedStudySite) {
        this.selectedStudySite = selectedStudySite;
    }

    public String getArmIndexToRemove() {
        return armIndexToRemove;
    }

    public void setArmIndexToRemove(String armIndexToRemove) {
        this.armIndexToRemove = armIndexToRemove;
    }

    public boolean isActiveDefaultArm() {
        return activeDefaultArm;
    }

    public boolean getActiveDefaultArm() {
        return activeDefaultArm;
    }

    public void setActiveDefaultArm(boolean activeDefaultArm) {
        this.activeDefaultArm = activeDefaultArm;
    }

    public void setDefaultOrganization(Organization organization) {
        this.defaultOrganization = organization;
    }

    public Organization getDefaultOrganization() {
        return defaultOrganization;
    }
}


