package gov.nih.nci.ctcae.core.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;

/**
 * @author mehul
 * Date: 7/7/11
 */
public class SecurityHelper {

    public static boolean isUserSiteIndependent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        boolean isLeadStaff = false;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.SITE_PI) || userRole.getRole().equals(Role.SITE_CRA) || userRole.getRole().equals(Role.NURSE) || userRole.getRole().equals(Role.TREATING_PHYSICIAN)) {
                isLeadStaff = false;
            } else {
                isLeadStaff = true;
            }
        }
        return isLeadStaff;
    }
    
    
    public static User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        
        return user;
    }
    
    /**
     * Update study privileges. Do this so that the CRA (or any role that can create a study) whose study privileges are pre-loaded 
     * is updated to include this newly created study. Prevents the creating lead_cra from being denied access to the study he just created.
     *
     * @param study the study
     */
    public static void updateLoadedStudyPrivileges(Study study) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User)auth.getPrincipal();
		DomainObjectPrivilegeGenerator privilegeGenerator = new DomainObjectPrivilegeGenerator();
		Set<Role> roles = new HashSet<Role>();
		for (UserRole userRole : user.getUserRoles()) {
            roles.add(userRole.getRole());
        }
		
		if(roles.contains(Role.CCA)){
			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(Arrays.asList(((User)auth.getPrincipal()).getAuthorities()));
			Set<String> privileges = privilegeGenerator.generatePrivilege(study);
            for (String privilege : privileges) {
                grantedAuthorities.add(new GrantedAuthorityImpl(privilege));
            }
            user.setGrantedAuthorities(grantedAuthorities.toArray(new GrantedAuthority[]{}));
        }
	}
    
}
