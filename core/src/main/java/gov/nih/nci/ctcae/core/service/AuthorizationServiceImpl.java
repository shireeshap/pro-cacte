package gov.nih.nci.ctcae.core.service;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author AmeyS
 * Used for providing instanceLevel security to the application.
 * It is used generate instanceSpecific privileges for user, based on their role on the studies
 */
public class AuthorizationServiceImpl implements AuthorizationService {
	protected GenericRepository genericRepository;
	protected Map<String, Boolean> filteredAccessPrivilegeMap;
	private static String STUDY_SECTION = "Study";
	private static String PARTICIPANT_SECTION = "Participant";
	protected Map<String, Boolean> studySectionPrivilegesMap;
	protected Map<String, Boolean> participantSectionPrivilegesMap;
	List<String> formSectionPrivilegesMap;
    List<String> administrationSectionPrivilegesMap;
    private static String STUDY = "study"; 
    private static String PARTICIPANT = "participant"; 
    

	@Override
	public List<String> processPrivilege(User user, String privilege, Map<Role, ArrayList<Integer>> studyRoleMap) {
		List<String> privilegeList = new ArrayList<String>();
		String privilegeName = privilege;
		privilegeList.add(privilegeName);
		if(filteredAccessPrivilegeMap.get(privilege) != null && !user.isAdmin()){
			generatePrivilegesForAccessibleObjects(user, privilegeList, privilegeName, studyRoleMap);
		}
		return privilegeList;
	}
	
	@Override
	public void generatePrivilegesForAccessibleObjects(User user, List<String> privilegeList, String privilegeName, Map<Role, ArrayList<Integer>> studyRoleMap) {
		String selectedSection = findSection(privilegeName);
		List<Role> rolesAllowedForPrivilege = new ArrayList<Role>();
		rolesAllowedForPrivilege = findRolesForPrivilege(user, privilegeName);
		
		if(STUDY_SECTION.equals(selectedSection)){
			Set<Integer> accessibleStudies = findAccessibleStudies(rolesAllowedForPrivilege, studyRoleMap);
			for(Integer id : accessibleStudies){
				privilegeList.add(privilegeName + AuthorizationServiceImpl.getStudyInstanceSpecificPrivilege(id));
			}
		} else if(PARTICIPANT_SECTION.equals(selectedSection)){
			Set<Integer> accessibleStudies = findAccessibleStudies(rolesAllowedForPrivilege, studyRoleMap);
			Set<Integer> accessibleParticipants = new HashSet<Integer>();
			List<Integer> participantIds = new ArrayList<Integer>();
			
			for(Integer studyId : accessibleStudies){
				boolean hasStudyLevelRole = hasStudyLevelRoleOnStudy(studyRoleMap, rolesAllowedForPrivilege, studyId);
				participantIds = fetchParticipantOnStudy(studyId, user, rolesAllowedForPrivilege, hasStudyLevelRole);
				accessibleParticipants.addAll((Collection<Integer>) participantIds);
			}
			
			for(Integer id : accessibleParticipants){
				privilegeList.add(privilegeName + AuthorizationServiceImpl.getParticipantInstanceSpecificPrivilege(id));
			}
		}
	}
	
	public static String getStudyInstanceSpecificPrivilege(Integer studyId){
		return "." + STUDY + "." + studyId;
	}
	
	public static String getParticipantInstanceSpecificPrivilege(Integer participantId){
		return "." + PARTICIPANT + "." + participantId;
	}
	
	
	/**fetchParticipantOnStudy() function
	 * Find participants on specified study such that the current loggedIn user at least of the roles provided in list.
	 * @param studyId
	 * @param user
	 * @param rolesAllowedForPrivilege
	 * @return
	 */
	public List<Integer> fetchParticipantOnStudy(Integer studyId, User user, List<Role> rolesAllowedForPrivilege, boolean hasStudyLevelRole){
		List<Integer> participantIds = new ArrayList<Integer>();
		ParticipantQuery query = new ParticipantQuery(false);
		if(hasStudyLevelRole){
			query.leftJoinForLeadRole();
		} else {
			query.leftJoinForSiteRole();
		}
		query.filterByStaffUsername(user.getUsername());
		query.filterByStudyId(studyId);
		query.filterByRole(rolesAllowedForPrivilege);
			
		List<Participant> participants = genericRepository.find(query);
		for(Participant p : participants){
			participantIds.add(p.getId());
		}
		return participantIds;
	}

	@Override
	public List<Role> findRolesForPrivilege(User user, String privilegeName) {
		List<Role> roles = new ArrayList<Role>();
		Map<String, List<Role>> userSpecificPrivilegeRoleMap = user.getUserSpecificPrivilegeRoleMap();
		roles = userSpecificPrivilegeRoleMap.get(privilegeName);
		return roles;
	}

	@Override
	public Set<Integer> findAccessibleStudies(List<Role> roles, Map<Role, ArrayList<Integer>> studyRoleMap) {
		Set<Integer> accessibleStudies = new HashSet<Integer>();
		List<Integer> studyIds;
		for(Role role : roles){
			if(studyRoleMap.get(role) != null){
				studyIds = studyRoleMap.get(role);
				accessibleStudies.addAll((Collection<Integer>) studyIds);
			}
		}
		return accessibleStudies;
	}
	
	@Override
	public boolean hasRole(Study study, List<Role> roles, User user) {
		boolean isRolePresent = false;
		if(user.isAdmin()){
			return true;
		}
		
		if(study != null && roles != null){
			List<StudyOrganizationClinicalStaff> socsList = study.getAllStudyOrganizationClinicalStaffs();
	    	for(StudyOrganizationClinicalStaff socs : socsList){
	    		if(socs.getOrganizationClinicalStaff().getClinicalStaff().getUser() != null){
	    			if(socs.getOrganizationClinicalStaff().getClinicalStaff().getUser().equals(user)){
		    			if(roles.contains(socs.getRole())){
		    				isRolePresent = true;
		    				break;
		    			}
		    		}
	    		}
	    	}
		}
    	return isRolePresent;
	}
	
	/**
	 * Check if the user has access to the passed-in privilege (@param: privilegeName) 
	 * for the given studyInstance (@param: study)
	 */
	@Override
	public boolean hasAccessForStudyInstance(User user, Study study, String privilegeName) {
		 List<Role> roles = findRolesForPrivilege(user, privilegeName);
	     return hasRole(study, roles, user);
	}
	
	/**hasStudyLevelRoleOnStudy() function.
	 * Check if the user has one of the allowed roles on the specified study and
	 * return true if that is not a site level role.
	 */
	@Override
	public boolean hasStudyLevelRoleOnStudy(Map<Role, ArrayList<Integer>> studyRoleMap,
			List<Role> rolesAllowedForPrivilege, Integer studyId) {
		for(Role role : studyRoleMap.keySet()){
			if(studyRoleMap.get(role).contains(studyId)){
				if(!(role.equals(Role.SITE_CRA) || role.equals(Role.SITE_PI) || 
	        			role.equals(Role.NURSE) || role.equals(Role.TREATING_PHYSICIAN))){
					return true;
				}
			}
		}
		return false;
	}
	
	public static Study getStudy(Participant participant){
		if(participant.getStudyParticipantAssignments().size() > 0){
			return participant.getStudyParticipantAssignments().get(0).getStudySite().getStudy();
		}
	    return null;
	}
	
	public static boolean isInstanceLevelSecurityRequired(User user){
    	if(user.isAdmin()){
    		return false;
    	}
    	return true;
    }
	
	public String findSection(String privilegeName){
		String selectedSection = null;
		if(studySectionPrivilegesMap.get(privilegeName) != null){
			selectedSection = STUDY_SECTION;
		} else if(participantSectionPrivilegesMap.get(privilegeName) != null){
			selectedSection = PARTICIPANT_SECTION;
		}
		return selectedSection;
	}
	
	public Map<String, Boolean> getFilteredAccessPrivilegeMap() {
		return filteredAccessPrivilegeMap;
	}
	
	public Map<String, Boolean> getStudySectionPrivilegesMap() {
		return studySectionPrivilegesMap;
	}
	
	public Map<String, Boolean> getParticipantSectionPrivilegesMap() {
		return participantSectionPrivilegesMap;
	}
	
   @Required
   public void setGenericRepository(GenericRepository genericRepository) {
       this.genericRepository = genericRepository;
   }
   
   @Required
   public void setFilteredAccessPrivilegeMap(Map<String, Boolean> filteredAccessPrivilegeMap) {
       this.filteredAccessPrivilegeMap = filteredAccessPrivilegeMap;
   }
   
   @Required
   public void setStudySectionPrivilegesMap(Map<String, Boolean> studySectionPrivilegesMap) {
       this.studySectionPrivilegesMap = studySectionPrivilegesMap;
   }
   
   @Required
   public void setParticipantSectionPrivilegesMap(Map<String, Boolean> participantSectionPrivilegesMap) {
       this.participantSectionPrivilegesMap = participantSectionPrivilegesMap;
   }
}
