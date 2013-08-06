package gov.nih.nci.ctcae.core.service;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;

/**
 * @author AmeyS
 * UserRoleServiceImpl is used to add userRoles of the overallStudyStaff (PI, ODC, LCRA) associated with a study.
 */
public class UserRoleServiceImpl implements UserRoleService {
	protected UserRepository userRepository;
	protected StudyRepository studyRepository;

	/*
	 * UserRoleService#addUserRoleForUpdatedLCRAorPI
	 * @param: odc, pI, previousLeadCraList contain the previously assigned
	 * 		   StudyOrganizationClinicalStaff for the role of ODC, PI and LeadCRA respectively
	 * @param: study object contains updated list of StudyOrganizationClinicalStaff
	 */
	public void addUserRoleForUpdatedLCRAorPI(Study study){
		User user;
		
		//studyId = null indicates first Save on studyDetailsTab in new study creation flow for which the below userRoleUpdation logic can be skipped.
		if(study.getId() != null){
			Study fetchedStudy = studyRepository.findById(study.getId());
			List<StudyOrganizationClinicalStaff> previousOdcList = fetchedStudy.getOverallDataCoordinators();
			List<StudyOrganizationClinicalStaff> previousPiList = fetchedStudy.getPrincipalInvestigators();
			List<StudyOrganizationClinicalStaff> previousLeadCraList = fetchedStudy.getLeadCRAs();
			
			if(previousOdcList.size() != 0 && study.getOverallDataCoordinators().size() != 0){
				//Delete the userRole for the ODC's removed from the study
				List<ClinicalStaff> clinicalStaffList = getClinicalStaffList(study.getOverallDataCoordinators());
				for(StudyOrganizationClinicalStaff socs : previousOdcList){
					if(!clinicalStaffList.contains(socs.getOrganizationClinicalStaff().getClinicalStaff())){
						removePreviousStaffUserRole(socs, socs.getRole());
					}
				}
				//Add userRole for newly added ODC's onto the Study
				clinicalStaffList = getClinicalStaffList(previousOdcList);
				for(StudyOrganizationClinicalStaff socs : study.getOverallDataCoordinators()){
					if(!clinicalStaffList.contains(socs.getOrganizationClinicalStaff().getClinicalStaff())){
						 user = getUserForStudyOrganizationClinicalStaff(socs);
						 addUserRole(user, socs.getRole());
					}
				}
			}//For the first save on OverallStudyStaffTab in study creation flow, simply add the userRole for all the ODC's, as no additional check is required 
			else if(previousOdcList.size() == 0 && study.getOverallDataCoordinators().size() != 0){
				for(StudyOrganizationClinicalStaff socs : study.getOverallDataCoordinators()){
					 user = getUserForStudyOrganizationClinicalStaff(socs);
					 addUserRole(user, socs.getRole());
				}
			}
			
			if(previousPiList.size() != 0 && study.getPrincipalInvestigators().size() != 0){
				//Delete the userRole for the PI's removed from the study
				List<ClinicalStaff> clinicalStaffList = getClinicalStaffList(study.getPrincipalInvestigators());
				for(StudyOrganizationClinicalStaff socs : previousPiList){
					if(!clinicalStaffList.contains(socs.getOrganizationClinicalStaff().getClinicalStaff())){
						removePreviousStaffUserRole(socs, socs.getRole());
					}
				}
				//Add userRole for newly added PI's onto the Study
				clinicalStaffList = getClinicalStaffList(previousPiList);
				for(StudyOrganizationClinicalStaff socs : study.getPrincipalInvestigators()){
					if(!clinicalStaffList.contains(socs.getOrganizationClinicalStaff().getClinicalStaff())){
						 user = getUserForStudyOrganizationClinicalStaff(socs);
						 addUserRole(user, socs.getRole());
					}
				}
			}//For the first save on OverallStudyStaffTab in study creation flow, simply add the userRole for all the PI's, as no additional check is required 
			else if(previousPiList.size() == 0 && study.getPrincipalInvestigators().size() != 0){
				for(StudyOrganizationClinicalStaff socs : study.getPrincipalInvestigators()){
					 user = getUserForStudyOrganizationClinicalStaff(socs);
					 addUserRole(user, socs.getRole());
				}
			}
			
			if(previousLeadCraList.size() != 0 && study.getLeadCRAs().size() != 0){
				//Delete the userRole for the LCRA's removed from the study
				List<ClinicalStaff> clinicalStaffList = getClinicalStaffList(study.getLeadCRAs());
				for(StudyOrganizationClinicalStaff socs : previousLeadCraList){
					if(!clinicalStaffList.contains(socs.getOrganizationClinicalStaff().getClinicalStaff())){
						removePreviousStaffUserRole(socs, socs.getRole());
					}
				}
				//Add userRole for newly added LCRA's onto the Study
				clinicalStaffList = getClinicalStaffList(previousLeadCraList);
				for(StudyOrganizationClinicalStaff socs : study.getLeadCRAs()){
					if(!clinicalStaffList.contains(socs.getOrganizationClinicalStaff().getClinicalStaff())){
						 user = getUserForStudyOrganizationClinicalStaff(socs);
						 addUserRole(user, socs.getRole());
					}
				}
			}//For the first save on OverallStudyStaffTab in study creation flow, simply add the userRole for all the LCRA's, as no additional check is required 
			else if(previousLeadCraList.size() == 0 && study.getLeadCRAs().size() != 0){
				for(StudyOrganizationClinicalStaff socs : study.getLeadCRAs()){
					 user = getUserForStudyOrganizationClinicalStaff(socs);
					 addUserRole(user, socs.getRole());
				}
			}
		}
	}
	
	private void addUserRole(User user, Role role){
		if(user != null){
			user.addUserRole(new UserRole(role));
			userRepository.saveOrUpdate(user, user.getPassword());
		}
	}
	
	private void removePreviousStaffUserRole(StudyOrganizationClinicalStaff socs, Role role){
		User user = getUserForStudyOrganizationClinicalStaff(socs);
		if(user != null){
			List<UserRole> userRoles = user.getUserRoles();
			for(UserRole ur : userRoles){
				if(ur.getRole().equals(role)){
					user.getUserRoles().remove(ur);
					userRepository.saveOrUpdate(user, user.getPassword());
					break;
				}
			}
		}
	}
	
	private List<ClinicalStaff> getClinicalStaffList(List<StudyOrganizationClinicalStaff> newLeadCraList){
		List<ClinicalStaff> cs = new ArrayList<ClinicalStaff>();
		for(StudyOrganizationClinicalStaff socs: newLeadCraList){
			cs.add(socs.getOrganizationClinicalStaff().getClinicalStaff());
		}
		return cs;
	}
	
	private User getUserForStudyOrganizationClinicalStaff(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff){
    	return studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff().getUser();
    }
	
	public void setUserRepository(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	public void setStudyRepository(StudyRepository studyRepository){
		this.studyRepository = studyRepository;
	}
}
