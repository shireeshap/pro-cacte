package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

public class RoleTest extends TestCase {

    public void testRole() {
        Role role = Role.TREATING_PHYSICIAN;
        assertEquals("TREATING_PHYSICIAN", role.toString());
    }

    public void testGetByCode() {

        Role role = Role.getByCode("TREATING_PHYSICIAN");
        assertNotNull(role);

    }
    
    public void testGetByDisplayNameAndRoleType(){
    	Role role = Role.ADMIN;
    	RoleType roleType = RoleType.ADMIN;
    	
    	assertEquals(Role.getByDisplayName("ADMIN"), Role.ADMIN);
    	assertEquals(role.getRoleType(), roleType);
    }
    
    public void testGetDisplayNameAndText(){
    	Role role = Role.ADMIN;
    	
    	assertEquals(role.getDisplayName(), "ADMIN");
    	assertEquals(role.getScreenText(), "Admin");
    }


}