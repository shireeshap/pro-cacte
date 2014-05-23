package gov.nih.nci.ctcae.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;

/**
 * @author AmeyS
 * AuthorizationServiceImpl class.
 * Testcases for AuthorizationServiceImpl
 */
public class AuthorizationServiceImplTest extends TestDataManager{
	
	private AuthorizationServiceImpl authorizationServiceImpl;
	private static String PRIVILEGE_ADD_STUDY_SITE = "PRIVILEGE_ADD_STUDY_SITE";
	private static String PRIVILEGE_CREATE_PARTICIPANT = "PRIVILEGE_CREATE_PARTICIPANT";
	private static String PRIVILEGE_CREATE_FORM = "PRIVILEGE_CREATE_FORM";
	private static String PI_USER_NAME = "ethan.basch@demo.com";
	Map<String, List<Role>> userSpecificPrivilegeRoleMap;
	Map<String, Boolean> studySectionPrivilegesMap;
	Map<String, Boolean> participantSectionPrivilegesMap;
	List<Role> roles;
	Map<Role, ArrayList<Integer>> studyRoleMap;
	ArrayList<Integer> studyIds;
	User user;
	List<String> processedPrivileges;
	UserRole userRole;
	Map<String, Boolean> filteredAccessPrivilegeMap;
	Integer studyId;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		authorizationServiceImpl = new AuthorizationServiceImpl();
		roles = new ArrayList<Role>();
		roles.add(Role.PI);
		userSpecificPrivilegeRoleMap = new HashMap<String, List<Role>>();
		userSpecificPrivilegeRoleMap.put(PRIVILEGE_ADD_STUDY_SITE, roles);
		userSpecificPrivilegeRoleMap.put(PRIVILEGE_CREATE_PARTICIPANT, roles);
		studyId = StudyTestHelper.getDefaultStudy().getId();
		studyRoleMap = new HashMap<Role, ArrayList<Integer>>();
		studyIds = new ArrayList<Integer>();
		studyIds.add(studyId);
		studyRoleMap.put(Role.PI, studyIds);
		user = findUserByUserName(PI_USER_NAME);
		processedPrivileges  = new ArrayList<String>();
		filteredAccessPrivilegeMap = new HashMap<String, Boolean>();
		filteredAccessPrivilegeMap.put(PRIVILEGE_ADD_STUDY_SITE, true);
		filteredAccessPrivilegeMap.put(PRIVILEGE_CREATE_PARTICIPANT, true);
		authorizationServiceImpl.setFilteredAccessPrivilegeMap(filteredAccessPrivilegeMap);
		studySectionPrivilegesMap = new HashMap<String, Boolean>();
		studySectionPrivilegesMap.put(PRIVILEGE_ADD_STUDY_SITE, true);
		authorizationServiceImpl.setStudySectionPrivilegesMap(studySectionPrivilegesMap);
		participantSectionPrivilegesMap = new HashMap<String, Boolean>();
		participantSectionPrivilegesMap.put(PRIVILEGE_CREATE_PARTICIPANT, true);
		authorizationServiceImpl.setParticipantSectionPrivilegesMap(participantSectionPrivilegesMap);
		authorizationServiceImpl.setGenericRepository(genericRepository);
	}

	public void testProcessPrivilege_StudySection(){
		
		//PRIVILEGE_CREATE_FORM privilege is not expected to be processed for applying instance level security
		processedPrivileges = authorizationServiceImpl.processPrivilege(user, PRIVILEGE_CREATE_FORM, studyRoleMap);
		assertEquals(1, processedPrivileges.size());
		assertTrue(processedPrivileges.contains(PRIVILEGE_CREATE_FORM));
		
		/**PRIVILEGE_ADD_STUDY_SITE privilege is processed by processPrivilege() method and instance specific privilege strings are generated 
		 * by appending accessible study ids.
		 */
		processedPrivileges = authorizationServiceImpl.processPrivilege(user, PRIVILEGE_ADD_STUDY_SITE, studyRoleMap);
		assertEquals(2, processedPrivileges.size());
		assertTrue(processedPrivileges.contains(PRIVILEGE_ADD_STUDY_SITE));
		assertTrue(processedPrivileges.contains(PRIVILEGE_ADD_STUDY_SITE+".study."+studyId));
	}
	
	public void testProcessPrivilege_ParticipantSection(){
		
		/**PRIVILEGE_CREATE_PARTICIPANT privilege is processed by processPrivilege() method and instance specific  privilege strings are generated 
		 * by appending accessible participant ids.
		 */
		processedPrivileges = authorizationServiceImpl.processPrivilege(user, PRIVILEGE_CREATE_PARTICIPANT, studyRoleMap);
		ParticipantQuery query = new ParticipantQuery(QueryStrings.PARTICIPANT_QUERY_COUNT, false);
		query.filterByStudy(studyId);
		Long count = genericRepository.findWithCount(query);
		assertEquals((++count).intValue(), processedPrivileges.size());
	}
	
	private User findUserByUserName(String userName){
		 return userRepository.loadUserByUsername(userName);
	}
}
