package gov.nih.nci.ctcae.core.service;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

	public interface AuthorizationService {
		List<String> processPrivilege(User user, String privilege, Map<Role, ArrayList<Integer>> studyRoleMap);
		void generatePrivilegesForAccessibleObjects(User user, List<String> privilegeList, String privilegeName, Map<Role, ArrayList<Integer>> studyRoleMap);
		List<Role> findRolesForPrivilege(User user, String privilegeName);
		Set<Integer> findAccessibleStudies(List<Role> rolesAllowedForPrivilege, Map<Role, ArrayList<Integer>> studyRoleMap);
		public List<Integer> fetchParticipantOnStudy(Integer studyId, User user, List<Role> rolesAllowedForPrivilege, boolean hasStudyLevelRole);
		public boolean hasRole(Study study, List<Role> roles, User user);
		public boolean hasAccessForStudyInstance(User user, Study study, String privilegeName);
		public boolean hasStudyLevelRoleOnStudy(Map<Role, ArrayList<Integer>> studyRoleMap, List<Role> rolesAllowedForPrivilege, Integer studyId);
	}
