package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;

/**
 * @author Vinay Kumar
 * @since Feb 11, 2009
 */
public class StudySiteClinicalStaffTab extends SecuredTab<StudyCommand> {

    private StudyRepository studyRepository;

    private StudyOrganizationRepository studyOrganizationRepository;
    private UserRepository userRepository;
    private GenericRepository genericRepository;

    public StudySiteClinicalStaffTab() {
        super("study.tab.study_site_clinical_staff", "study.tab.study_site_clinical_staff", "study/study_site_clinical_staff");
    }


    @Override
    public void onDisplay(HttpServletRequest request, StudyCommand command) {
        super.onDisplay(request, command);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.isAdmin()) {
            ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
            command.setDefaultOrganization(clinicalStaff.getOrganizationClinicalStaffs().get(0).getOrganization());
        }
    }


    @Override
    public Map<String, Object> referenceData(StudyCommand command) {
        Map<String, Object> referenceData = super.referenceData(command);

        StudyOrganizationQuery query = new StudyOrganizationQuery();
        query.filterByStudyId(command.getStudy().getId());
        query.filterByStudySiteAndLeadSiteOnly();

        List<StudyOrganization> studySites = (List<StudyOrganization>) studyOrganizationRepository.find(query);
        if (!studySites.isEmpty() && command.getSelectedStudySite() == null) {
            for (StudyOrganization studySite : studySites) {
                if (studySite.getOrganization().equals(command.getDefaultOrganization())) {
                    command.setSelectedStudySite((StudySite) studySite);
                    break;
                }
            }
            if(command.getSelectedStudySite() == null){
            	command.setSelectedStudySite((StudySite) studySites.iterator().next());
            }
        }
        referenceData.put("studySites", studySites);
        referenceData.put("roleStatusOptions", ListValues.getRoleStatusType());
        referenceData.put("notifyOptions", ListValues.getNotificationRequired());

        return referenceData;
    }


    @Override
    //saves the studyOrg and is excluded in the shouldSave() so that the whole study doesnt have to be saved.
    public void postProcess(HttpServletRequest request, StudyCommand command, Errors errors) {
    	boolean saveStudyOrganization = false;
    	for(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : command.getNewlyAddedSocsForSelectedSite()) {
    		if(studyOrganizationClinicalStaff != null){
    			saveStudyOrganization = true;

    			if(studyOrganizationClinicalStaff.getRole().equals(Role.SITE_CRA) || 
    					studyOrganizationClinicalStaff.getRole().equals(Role.SITE_PI)){
    				addStaffToAllValidStudyOrganizations(studyOrganizationClinicalStaff, command.getStudy());
    			} else {
    				addStaffOnlyToGivenStudyOrganization(studyOrganizationClinicalStaff, command.getStudy());
    			}
    		}
        }
        if(saveStudyOrganization){
            command.setStudy(studyRepository.findById(command.getStudy().getId()));
            command.updateClinicalStaffs();
            command.removeAllNewSocsForSelectedSite();
            request.setAttribute("flashMessage", "save.confirmation");
        }
        
        List<StudyOrganizationClinicalStaff> socsWithToggledNotifyOption = getSocsWithToggledNotifyOption(command, command.getStudy());
        for(StudyOrganizationClinicalStaff socs : socsWithToggledNotifyOption){
        	genericRepository.save(socs);
        }
    }
    
    private List<StudyOrganizationClinicalStaff> getSocsWithToggledNotifyOption(StudyCommand command, Study study){
    	List<StudyOrganizationClinicalStaff> socsWithToggledNotifyOption = new ArrayList<StudyOrganizationClinicalStaff>();
    	StudySite studySite = command.getSelectedStudySite();
    	if(studySite != null){
    		for(StudyOrganizationClinicalStaff socs : command.getStudyOrganizationClinicalStaffs()){
    			if(socs != null){
    				if(studySite.getId().equals(socs.getStudyOrganization().getId())){
    					for(StudyOrganizationClinicalStaff studySocs : study.getAllStudyOrganizationClinicalStaffs()){
    						if(studySocs.equals(socs) && !studySocs.getNotify().equals(socs.getNotify())){
    							socsWithToggledNotifyOption.add(socs);
    						}
    					}
    				}
    			}
    		}
    	}
    	
    	return socsWithToggledNotifyOption;
    }

    /*
     * Add staff only to the study organization affiliated with the studyOrganizationClinicalStaff
     */
    private void addStaffOnlyToGivenStudyOrganization(
			StudyOrganizationClinicalStaff studyOrganizationClinicalStaff, Study study) {
    	studyOrganizationClinicalStaff.getStudyOrganization().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
		studyOrganizationRepository.save(study.getStudyOrganization(studyOrganizationClinicalStaff));		
	}


	/*
     * For a given organizationClinicalStaff, add him as a study site staff to all
     * his sites that are study orgs on the given study.
     */
    private void addStaffToAllValidStudyOrganizations(
			StudyOrganizationClinicalStaff studyOrganizationClinicalStaff, Study study) {
    	ClinicalStaff clinicalStaff = studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff();
    	for(OrganizationClinicalStaff ocs : clinicalStaff.getOrganizationClinicalStaffs()){
    		StudyOrganization matchingStudyOrg = isCliniciansOrgAValidStudySite(ocs, study, studyOrganizationClinicalStaff.getRole());
    		if(matchingStudyOrg != null){
                StudyOrganizationClinicalStaff newSocs = new StudyOrganizationClinicalStaff();
                newSocs.setRole(studyOrganizationClinicalStaff.getRole());
                newSocs.setNotify(studyOrganizationClinicalStaff.getNotify());
                newSocs.setStudyOrganization(matchingStudyOrg);
                newSocs.setOrganizationClinicalStaff(ocs);
                if(!matchingStudyOrg.getStudyOrganizationClinicalStaffs().contains(newSocs)){
                    matchingStudyOrg.getStudyOrganizationClinicalStaffs().add(newSocs);
                }
	    		studyOrganizationRepository.save(matchingStudyOrg);
    		}
    	}		
	}


	/*
     * If given ocs's site is a study site or lead study site on the study, 
     * and if the clinicalStaff isnt already assigned to that study site then return that study site.
     */
    private StudyOrganization isCliniciansOrgAValidStudySite(OrganizationClinicalStaff ocs,
			Study study, Role role) {
    	boolean alreadyExists = false;
    	for(StudySite ss : study.getStudySites()){
    		if(ss.getOrganization().equals(ocs.getOrganization())){
    			for(StudyOrganizationClinicalStaff socsAlreadyOnStudy : ss.getStudyOrganizationClinicalStaffByRole(role)){
    				if(socsAlreadyOnStudy.getOrganizationClinicalStaff().getClinicalStaff().equals(ocs.getClinicalStaff())){
    					alreadyExists = true;
    					break;
    				}
    			}
    			if(!alreadyExists){
        			return ss;
    			}
    		}
    	}
    	if(study.getLeadStudySite().getOrganization().equals(ocs.getOrganization())){
    		for(StudyOrganizationClinicalStaff socsAlreadyOnStudy : study.getLeadStudySite().getStudyOrganizationClinicalStaffByRole(role)){
				if(socsAlreadyOnStudy.getOrganizationClinicalStaff().getClinicalStaff().equals(ocs.getClinicalStaff())){
					alreadyExists = true;
					break;
				}
			}
    		if(!alreadyExists){
    			return study.getLeadStudySite();
    		}
		}
    	return null;
	}


	public String getRequiredPrivilege() {
    	return Privilege.PRIVILEGE_ADD_STUDY_SITE_STAFF;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void setGenericRepository(GenericRepository genericRepository){
    	this.genericRepository = genericRepository;
    }
}