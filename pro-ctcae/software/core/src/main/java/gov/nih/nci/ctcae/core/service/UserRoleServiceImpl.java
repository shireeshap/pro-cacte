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
 *
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
		
		Study fetchedStudy = studyRepository.findById(study.getId());
		StudyOrganizationClinicalStaff odc = fetchedStudy.getOverallDataCoordinator();
		StudyOrganizationClinicalStaff pI = fetchedStudy.getPrincipalInvestigator();
		List<StudyOrganizationClinicalStaff> previousLeadCraList = fetchedStudy.getLeadCRAs();
		
		//Add userRoles for updated ODC
		if (!odc.getOrganizationClinicalStaff().getClinicalStaff()
				.equals(study.getOverallDataCoordinator().getOrganizationClinicalStaff().getClinicalStaff())) {
			
			removePreviousStaffUserRole(odc, odc.getRole());
			user = getUserForStudyOrganizationClinicalStaff(study.getOverallDataCoordinator());
			addUserRole(user, study.getOverallDataCoordinator().getRole());
		}
		
		//Add userRoles for updated PI
		if (!pI.getOrganizationClinicalStaff().getClinicalStaff()
				.equals(study.getPrincipalInvestigator().getOrganizationClinicalStaff().getClinicalStaff())) {
			
			removePreviousStaffUserRole(pI, pI.getRole());
			user = getUserForStudyOrganizationClinicalStaff(study.getPrincipalInvestigator());
			addUserRole(user, study.getPrincipalInvestigator().getRole());
		}
		
		//Delete the userRole for the LCRA's removed from the study
		List<ClinicalStaff> clinicalStaffList = getClinicalStaffList(study.getLeadCRAs());
		for(StudyOrganizationClinicalStaff socs : previousLeadCraList){
			if(!clinicalStaffList.contains(socs.getOrganizationClinicalStaff().getClinicalStaff())){
				removePreviousStaffUserRole(socs, socs.getRole());
			}
		}
		//Add userRole for newly added LCRA's
		clinicalStaffList = getClinicalStaffList(previousLeadCraList);
		for(StudyOrganizationClinicalStaff socs : study.getLeadCRAs()){
			if(!clinicalStaffList.contains(socs.getOrganizationClinicalStaff().getClinicalStaff())){
				 user = getUserForStudyOrganizationClinicalStaff(socs);
				 addUserRole(user, socs.getRole());
			}
		}
	}
	
	private void addUserRole(User user, Role role){
		user.addUserRole(new UserRole(role));
		userRepository.saveOrUpdate(user, user.getPassword());
	}
	
	private void removePreviousStaffUserRole(StudyOrganizationClinicalStaff socs, Role role){
		User user = getUserForStudyOrganizationClinicalStaff(socs);
		List<UserRole> userRoles = user.getUserRoles();
		for(UserRole ur : userRoles){
			if(ur.getRole().equals(role)){
				user.getUserRoles().remove(ur);
				userRepository.saveOrUpdate(user, user.getPassword());
				break;
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
