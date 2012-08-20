package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class StudyCommand.
 *
 * @author Vinay Gangoli
 * @since Oct 27, 2008
 */
public class StudyCommand {

    /**
     * The study.
     */
    private Study study;

    private String[] appModes;

    
    private StudySite selectedStudySite;

    private List<StudyOrganizationClinicalStaff> leadCRAs = new ArrayList<StudyOrganizationClinicalStaff>();

    private StudyOrganizationClinicalStaff overallDataCoordinator;

    private StudyOrganizationClinicalStaff principalInvestigator;

    private boolean isAdmin = false;

    private List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = new ArrayList<StudyOrganizationClinicalStaff>();

    List<Organization> organizationsWithCCARole = new ArrayList<Organization>();

    private List<String> armIndicesToRemove = new ArrayList<String>();
    private boolean activeDefaultArm = false;
    private Organization defaultOrganization;


    private List<Integer> siteIndexesToRemove = new ArrayList<Integer>();
    private List<Integer> craIndexesToRemove = new ArrayList<Integer>();
    private boolean odc;

    public boolean isOdc() {
        return odc;
    }

    public void setOdc(boolean odc) {
        this.odc = odc;
    }

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

    public List<Organization> getOrganizationsWithCCARole() {
          return organizationsWithCCARole;
      }

      public void setOrganizationsWithCCARole(List<Organization> organizationsWithCCARole) {
          this.organizationsWithCCARole = organizationsWithCCARole;
      }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    public List<StudyOrganizationClinicalStaff> getLeadCRAs() {

        if (leadCRAs == null || leadCRAs.size() == 0) {
            leadCRAs = study.getLeadCRAs();
            if (leadCRAs == null || leadCRAs.size() == 0) {
            	StudyOrganizationClinicalStaff socs = new StudyOrganizationClinicalStaff();
            	socs.setRole(Role.LEAD_CRA);
            	leadCRAs.add(socs);
            }
        }
        return leadCRAs;
    }


    public void setLeadCRAs(List<StudyOrganizationClinicalStaff> leadCRAs) {
        this.leadCRAs = leadCRAs;

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

        setLeadCRAs(study.getLeadCRAs());
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

    public List<Integer> getSiteIndexesToRemove() {
        return siteIndexesToRemove;
    }

    public String[] getAppModes() {
        return appModes;
    }

    public void setAppModes(String[] appModes) {
        this.appModes = appModes;
    }

	public void setCraIndexesToRemove(List<Integer> craIndexesToRemove) {
		this.craIndexesToRemove = craIndexesToRemove;
	}

	public List<Integer> getCraIndexesToRemove() {
		return craIndexesToRemove;
	}

	public List<String> getArmIndicesToRemove() {
		return armIndicesToRemove;
	}

	public void setArmIndicesToRemove(List<String> armIndicesToRemove) {
		this.armIndicesToRemove = armIndicesToRemove;
	}
}


