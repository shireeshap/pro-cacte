package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.ArrayList;

public class RoleTest extends TestCase {

    public void testRole() {
        Role role = Role.TREATING_PHYSICIAN;
        assertEquals("TREATING_PHYSICIAN", role.toString());
    }

    public void testGetByCode(){

        Role role = Role.getByCode("TREATING_PHYSICIAN");
        assertNotNull(role);

    }


}