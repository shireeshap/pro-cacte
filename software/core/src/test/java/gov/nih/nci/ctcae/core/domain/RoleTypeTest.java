package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 */
public class RoleTypeTest extends TestCase {

    public void testStatus() {
        RoleType roleType = RoleType.SITE_LEVEL;
        assertEquals("SITE_LEVEL", roleType.toString());
        roleType = RoleType.STUDY_LEVEL;
        assertEquals("STUDY_LEVEL", roleType.toString());
        roleType = RoleType.STUDY_SITE_LEVEL;
        assertEquals("STUDY_SITE_LEVEL", roleType.toString());
       roleType = RoleType.STUDY_COORDINATING_CENTER_LEVEL;
        assertEquals("STUDY_COORDINATING_CENTER_LEVEL", roleType.toString());
    }

    public void testGetByCode() {
        assertEquals(RoleType.STUDY_COORDINATING_CENTER_LEVEL, RoleType.getByCode("STUDY_COORDINATING_CENTER_LEVEL"));
        assertEquals(RoleType.STUDY_SITE_LEVEL, RoleType.getByCode("STUDY_SITE_LEVEL"));
        assertEquals(RoleType.STUDY_LEVEL, RoleType.getByCode("STUDY_LEVEL"));
        assertEquals(RoleType.SITE_LEVEL, RoleType.getByCode("SITE_LEVEL"));

    }


}