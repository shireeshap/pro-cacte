package gov.nih.nci.ctcae.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;

	public interface AuthorizationService {
		List<String> processPrivilege(User user, String privilege, Map<Role, ArrayList<Integer>> studyRoleMap);
		void generatePrivilegesForAccessibleObjects(User user, List<String> privilegeList, String privilegeName, Map<Role, ArrayList<Integer>> studyRoleMap);
		List<Role> findRolesForPrivilege(User user, String privilegeName);
		Set<Integer> findAccessibleStudies(List<Role> rolesAllowedForPrivilege, Map<Role, ArrayList<Integer>> studyRoleMap);
		public List<Integer> fetchParticipantOnStudy(List<Integer> studyIds);
		public boolean hasRole(Study study, List<Role> roles, User user);
	}
