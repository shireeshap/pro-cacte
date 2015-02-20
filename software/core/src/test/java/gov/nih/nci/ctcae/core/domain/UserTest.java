package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @since Mar 12, 2009
 */
public class UserTest extends TestCase {

    private User user;
    Map<String, List<Role>> userSpecificPrivilegeRoleMap;
    public static final String PRIVILEGE_CREATE_STUDY = "PRIVILEGE_CREATE_STUDY";
    public static final String PRIVILEGE_ADD_STUDY_SITE = "PRIVILEGE_ADD_STUDY_SITE";
    private List<Role> rolesCreateStudy;
    private List<Role> rolesAddStudtSite;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        GrantedAuthority[] grantedAuthorities = new GrantedAuthority[]{new GrantedAuthorityImpl(Organization.class.getName() + "." + 1),
                new GrantedAuthorityImpl(Organization.class.getName() + "." + 2), new GrantedAuthorityImpl(Study.class.getName() + "." + 3)};
        user = new User();
        user.setGrantedAuthorities(grantedAuthorities);
        userSpecificPrivilegeRoleMap = new HashMap<String, List<Role>>();
        rolesCreateStudy = new ArrayList<Role>();
        rolesCreateStudy.add(Role.ADMIN);
        rolesCreateStudy.add(Role.PI);
        userSpecificPrivilegeRoleMap.put(PRIVILEGE_CREATE_STUDY, rolesCreateStudy);
        rolesAddStudtSite = new ArrayList<Role>();
        rolesAddStudtSite.add(Role.PI);
        userSpecificPrivilegeRoleMap.put(PRIVILEGE_ADD_STUDY_SITE, rolesAddStudtSite);
    }

    public void testGetAllowedOrganizationId() {

        List<Integer> organizationIds = user.findAccessibleObjectIds(Organization.class);
        assertTrue(organizationIds.contains(1));
        assertTrue(organizationIds.contains(2));
    }

    public void testGetAllowedStudyId() {

        List<Integer> organizationIds = user.findAccessibleObjectIds(Study.class);
        assertTrue(organizationIds.contains(3));
    }
    
    public void testGetUserSpecificPrivilegeRoleMap(){
    	Map<String, List<Role>> map;
    	List<Role> roles;
    	user.setUserSpecificPrivilegeRoleMap(userSpecificPrivilegeRoleMap);
    	
    	map = user.getUserSpecificPrivilegeRoleMap();
    	roles = map.get(PRIVILEGE_CREATE_STUDY);
    	assertTrue(roles.contains(Role.ADMIN));
    	
    	roles = map.get(PRIVILEGE_ADD_STUDY_SITE);
    	assertFalse(roles.contains(Role.TREATING_PHYSICIAN));
    	
    }
}
