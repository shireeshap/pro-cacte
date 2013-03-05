package gov.nih.nci.ctcae.core.service;

import java.util.List;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.repository.UserRepository;

public class UserRoleServiceImpl implements UserRoleService {
	protected UserRepository userRepository;
	
	public void addUserRoleForUpdatedLCRAorPI(Study study){
		User user;

		//Add userRoles for updated ODC
		user = getUserForStudyOrganizationClinicalStaff(study.getOverallDataCoordinator());
		addUserRole(user, study.getOverallDataCoordinator().getRole());
		
		//Add userRoles for updated PI
		user = getUserForStudyOrganizationClinicalStaff(study.getPrincipalInvestigator());
		addUserRole(user, study.getPrincipalInvestigator().getRole());
		
		//Add userRole for newly added LCRA's 
		for(StudyOrganizationClinicalStaff socs : study.getLeadCRAs()){
		 user = getUserForStudyOrganizationClinicalStaff(socs);
		 addUserRole(user, socs.getRole());
		}
	}
	
	public void addUserRole(User user, Role role){
		user.addUserRole(new UserRole(role));
		userRepository.saveOrUpdate(user, user.getPassword());
	}
	
	private User getUserForStudyOrganizationClinicalStaff(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff){
    	return studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff().getUser();
    }
	
	public void setUserRepository(UserRepository userRepository){
		this.userRepository = userRepository;
	}
}
