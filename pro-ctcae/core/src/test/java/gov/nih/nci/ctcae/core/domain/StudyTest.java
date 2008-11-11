package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
public class StudyTest extends TestCase {
    private Study study;

    public void testConstructor() {
        study = new Study();
        assertNull(study.getShortTitle());
        assertNull(study.getAssignedIdentifier());
        assertNull(study.getDescription());
    }

    public void testGetterAndSetter() {
        study = new Study();
        study.setShortTitle("short title");
        study.setAssignedIdentifier("identifier");
        study.setDescription("desc");
        assertEquals("short title", study.getShortTitle());
        assertEquals("identifier", study.getAssignedIdentifier());
        assertEquals("desc", study.getDescription());
    }

    public void testGetDisplayName() {
        study = new Study();
        study.setShortTitle("short title");
        assertEquals("short title", study.getDisplayName());

        study.setAssignedIdentifier("identifier");

        assertEquals("short title (identifier)", study.getDisplayName());
    }

    public void testEqualsAndHashCode() {
        Study anotherOrganization = null;
        assertEquals(anotherOrganization, study);
        study = new Study();
        assertFalse(study.equals(anotherOrganization));
        anotherOrganization = new Study();
        assertEquals(anotherOrganization, study);
        assertEquals(anotherOrganization.hashCode(), study.hashCode());


        study.setShortTitle("short title");
        assertFalse(study.equals(anotherOrganization));
        anotherOrganization.setShortTitle("short title");
        assertEquals(anotherOrganization.hashCode(), study.hashCode());
        assertEquals(anotherOrganization, study);

        study.setAssignedIdentifier("identifier");
        assertFalse(study.equals(anotherOrganization));
        anotherOrganization.setAssignedIdentifier("identifier");
        assertEquals(anotherOrganization.hashCode(), study.hashCode());
        assertEquals(anotherOrganization, study);

    }

}