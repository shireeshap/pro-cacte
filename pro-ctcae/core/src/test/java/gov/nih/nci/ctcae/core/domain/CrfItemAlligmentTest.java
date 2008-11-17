package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @created Nov 17, 2008
 */
public class CrfItemAlligmentTest extends TestCase {
    public void testStatus() {
        CrfItemAllignment crfItemAllignment = CrfItemAllignment.HORIZONTAL;
        assertEquals("Horizontal", crfItemAllignment.toString());
        crfItemAllignment = CrfItemAllignment.VERTICAL;
        assertEquals("Vertical", crfItemAllignment.toString());
    }

    public void testGetByCode() {
        assertEquals(CrfItemAllignment.HORIZONTAL, CrfItemAllignment.getByCode("Horizontal"));
        assertEquals(CrfItemAllignment.VERTICAL, CrfItemAllignment.getByCode("Vertical"));

    }

}