package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueObjectInCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<StudyOrganizationClinicalStaff> overallDataCoordinators = new ArrayList<StudyOrganizationClinicalStaff>();

    private List<StudyOrganizationClinicalStaff> principalInvestigators = new ArrayList<StudyOrganizationClinicalStaff>();

    private boolean isAdmin = false;

    private List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffs = new ArrayList<StudyOrganizationClinicalStaff>();
    
    private List<StudyOrganizationClinicalStaff> newSocsForSelectedSite = new ArrayList<StudyOrganizationClinicalStaff>();
    
    private Map<Integer, Integer> socsIndexMap = new HashMap<Integer, Integer>();

	List<Organization> organizationsWithCCARole = new ArrayList<Organization>();

    private List<String> armIndicesToRemove = new ArrayList<String>();
    private boolean activeDefaultArm = false;
    private Organization defaultOrganization;

    private List<Arm> nonDefaultArms = new ArrayList<Arm>();

    private List<Integer> siteIndexesToRemove = new ArrayList<Integer>();
    private List<Integer> lcraIndexesToRemove = new ArrayList<Integer>();
    private List<Integer> odcIndexesToRemove = new ArrayList<Integer>();
    private List<Integer> piIndexesToRemove = new ArrayList<Integer>();
    
    private boolean odc;
    private String studyInstanceSpecificPrivilege;

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
        this.setStudyInstanceSpecificPrivilege();
    }

    public StudyCommand(Study study) {
        this.study = study;
        this.setStudyInstanceSpecificPrivilege();
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
    
    public void setStudyInstanceSpecificPrivilege(){
    	this.studyInstanceSpecificPrivilege = AuthorizationServiceImpl.getStudyInstanceSpecificPrivilege(study.getId());
    }
    
    public String getStudyInstanceSpecificPrivilege(){
    	return studyInstanceSpecificPrivilege;
    }


    public List<StudyOrganizationClinicalStaff> getOverallDataCoordinators() {
    	if(overallDataCoordinators == null || overallDataCoordinators.size() == 0){
    		overallDataCoordinators = study.getOverallDataCoordinators();
    		if(overallDataCoordinators == null || overallDataCoordinators.size() == 0){
    			StudyOrganizationClinicalStaff socs = new StudyOrganizationClinicalStaff();
            	socs.setRole(Role.ODC);
            	overallDataCoordinators.add(socs);
    		}
    	}
    	
    	return overallDataCoordinators;
    }
    
    public void setOverallDataCoordinators(List<StudyOrganizationClinicalStaff> overallDataCoordinators) {
        this.overallDataCoordinators = overallDataCoordinators;

    }

    public List<StudyOrganizationClinicalStaff> getPrincipalInvestigators() {
    	if(principalInvestigators == null || principalInvestigators.size() == 0){
    		principalInvestigators = study.getPrincipalInvestigators();
    		if(principalInvestigators == null || principalInvestigators.size() == 0){
    			StudyOrganizationClinicalStaff socs = new StudyOrganizationClinicalStaff();
            	socs.setRole(Role.PI);
            	principalInvestigators.add(socs);
    		}
    	}
    	
    	return principalInvestigators;
    }
    
    public void setPrincipalInvestigators(List<StudyOrganizationClinicalStaff> principalInvestigators) {
        this.principalInvestigators = principalInvestigators;

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
        setPrincipalInvestigators(study.getPrincipalInvestigators());
        setOverallDataCoordinators(study.getOverallDataCoordinators());
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

    public List<StudyOrganizationClinicalStaff> getNewlyAddedSocsForSelectedSite() {
		return newSocsForSelectedSite;
	}
    
    public Map<Integer, Integer> getSocsIndexMap() {
		return socsIndexMap;
	}
    
    public void addToSocsIndexMap(Integer socsIndex, Integer newlyAddedSocsIndex) {
		getSocsIndexMap().put(socsIndex, newlyAddedSocsIndex);
	}
    
    public Integer getPositionFromSocsIndexMap(Integer socsIndex) {
		return getSocsIndexMap().get(socsIndex);
	}
    
    public void addSocsToSelectedSite(StudyOrganizationClinicalStaff socs) {
    	getNewlyAddedSocsForSelectedSite().add(socs);
	}
    
    public void removeAllNewSocsForSelectedSite() {
    	getNewlyAddedSocsForSelectedSite().clear();
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

	public void setLCRAIndexesToRemove(List<Integer> lcraIndexesToRemove) {
		this.lcraIndexesToRemove = lcraIndexesToRemove;
	}

	public List<Integer> getLCRAIndexesToRemove() {
		return lcraIndexesToRemove;
	}

	public void setPiIndexesToRemove(List<Integer> piIndexesToRemove) {
		this.piIndexesToRemove = piIndexesToRemove;
	}

	public List<Integer> getPiIndexesToRemove() {
		return piIndexesToRemove;
	}
	
	public void setOdcIndexesToRemove(List<Integer> odcIndexesToRemove) {
		this.odcIndexesToRemove = odcIndexesToRemove;
	}

	public List<Integer> getOdcIndexesToRemove() {
		return odcIndexesToRemove;
	}
	
	public List<String> getArmIndicesToRemove() {
		return armIndicesToRemove;
	}

	public void setArmIndicesToRemove(List<String> armIndicesToRemove) {
		this.armIndicesToRemove = armIndicesToRemove;
	}

	public List<Arm> getNonDefaultArms() {
		return nonDefaultArms;
	}

	public void setNonDefaultArms(List<Arm> nonDefaultArms) {
		this.nonDefaultArms = nonDefaultArms;
	}
}


