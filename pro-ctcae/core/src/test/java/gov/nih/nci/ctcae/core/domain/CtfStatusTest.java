package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * @author Vinay Kumar
 */
public class CtfStatusTest extends TestCase {

    public void testStatus() {
        CrfStatus status = CrfStatus.DRAFT;
        assertEquals("Draft", status.toString());
        status = CrfStatus.RELEASEED;
        assertEquals("Released", status.toString());
        status = CrfStatus.INPROGRESS;
        assertEquals("In-progress", status.toString());
    }

    public void testGetByCode() {
        assertEquals(CrfStatus.DRAFT, CrfStatus.getByCode("Draft"));
        assertEquals(CrfStatus.RELEASEED, CrfStatus.getByCode("Released"));
        assertEquals(CrfStatus.INPROGRESS, CrfStatus.getByCode("In-progress"));

    }


}