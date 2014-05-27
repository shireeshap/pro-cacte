package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Arm;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.DataCoordinatingCenter;
import gov.nih.nci.ctcae.core.domain.FundingSponsor;
import gov.nih.nci.ctcae.core.domain.LeadStudySite;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.StudySponsor;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.service.AuthorizationServiceImpl;

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

    private Study study;
    private String[] appModes;
    private StudySite selectedStudySite;
    private List<StudyOrganizationClinicalStaff> leadCRAs = new ArrayList<StudyOrganizationClinicalStaff>();
    private List<StudyOrganizationClinicalStaff> overallDataCoordinators = new ArrayList<StudyOrganizationClinicalStaff>();
    private List<StudyOrganizationClinicalStaff> principalInvestigators = new ArrayList<StudyOrganizationClinicalStaff>();
    private boolean isAdmin = false;
    private boolean CCA = false;
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
    private boolean editLeadSite = true;


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
    
    public boolean isOdc() {
    	return odc;
    }
    
    public void setOdc(boolean odc) {
    	this.odc = odc;
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
    
    public boolean isCCA() {
        return isAdmin;
    }

    public void setCCA(boolean admin) {
        isAdmin = admin;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
    
    public void setStudyInstanceSpecificPrivilege(){
    	this.studyInstanceSpecificPrivilege = AuthorizationServiceImpl.getStudyInstanceSpecificPrivilege(study.getId());
    }
    
    public String getStudyInstanceSpecificPrivilege(){
    	return studyInstanceSpecificPrivilege;
    }
    
    public boolean isEditLeadSite() {
		return editLeadSite;
	}

	public void setEditLeadSite(boolean editLeadSite) {
		this.editLeadSite = editLeadSite;
	}

    public List<StudyOrganizationClinicalStaff> getOverallDataCoordinators() {
    	if(overallDataCoordinators.isEmpty()){
    		overallDataCoordinators = study.getOverallDataCoordinators();
    		if(overallDataCoordinators.isEmpty()){
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
    	if(principalInvestigators.isEmpty()){
    		principalInvestigators = study.getPrincipalInvestigators();
    		if(principalInvestigators.isEmpty()){
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
        if (leadCRAs.isEmpty()) {
            leadCRAs = study.getLeadCRAs();
            if (leadCRAs.isEmpty()) {
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

    public Study getStudy() {
        return study;
    }

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

	//Note: this needs a live hibernate session to run successfully
	public void initailizeStudy() {
        for(StudyOrganization so : getStudy().getStudyOrganizations()){
        	for(StudyOrganizationClinicalStaff socs : so.getStudyOrganizationClinicalStaffs()){
        		ClinicalStaff cs = socs.getOrganizationClinicalStaff().getClinicalStaff();
        		cs.getOrganizationClinicalStaffs().size();
        	}
        }	
        getStudy().getArms().size();

	}
	
    public boolean isAnyParticipantPresent(GenericRepository genericRepository, Integer studyId){
    	Study study = (Study) genericRepository.findById(Study.class, studyId);
    	if(study.getLeadStudySite().getStudyParticipantAssignments().size() > 0){
    		return false;
    	}
    	return true;
    }
}