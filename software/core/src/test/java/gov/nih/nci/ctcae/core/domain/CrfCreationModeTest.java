package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 */
public class CrfCreationModeTest extends TestCase {

    public void testCreationMode() {
        CrfCreationMode status = CrfCreationMode.BASIC;
        assertEquals("Basic", status.toString());
        status = CrfCreationMode.ADVANCE;
        assertEquals("Advance", status.toString());
    }

    public void testGetByCode() {
        assertEquals(CrfCreationMode.BASIC, CrfCreationMode.getByCode("Basic"));
        assertEquals(CrfCreationMode.ADVANCE, CrfCreationMode.getByCode("Advance"));

    }


}