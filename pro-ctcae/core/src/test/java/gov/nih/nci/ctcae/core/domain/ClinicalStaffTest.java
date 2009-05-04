package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author mehul
 */

public class ClinicalStaffTest extends TestCase {
    private ClinicalStaff clinicalStaff;

    public void testConstructor() {
        clinicalStaff = new ClinicalStaff();
        assertNull(clinicalStaff.getFirstName());
        assertNull(clinicalStaff.getLastName());
    }

    public void testGetterAndSetter() {
        clinicalStaff = new ClinicalStaff();
        clinicalStaff.setFirstName("john");
        clinicalStaff.setLastName("dow");
        clinicalStaff.setMiddleName("mid");
        clinicalStaff.setEmailAddress("a@a.com");
        clinicalStaff.setFaxNumber("123 345");
        clinicalStaff.setNciIdentifier("NCI 1");
        clinicalStaff.setPhoneNumber("123 123");

        assertEquals("john", clinicalStaff.getFirstName());
        assertEquals("dow", clinicalStaff.getLastName());
        assertEquals("mid", clinicalStaff.getMiddleName());
        assertEquals("a@a.com", clinicalStaff.getEmailAddress());
        assertEquals("123 345", clinicalStaff.getFaxNumber());
        assertEquals("NCI 1", clinicalStaff.getNciIdentifier());
        assertEquals("123 123", clinicalStaff.getPhoneNumber());
    }

    public void testEqualsAndHashCode() {
        ClinicalStaff anotherClinicalStaff = null;
        assertEquals(anotherClinicalStaff, clinicalStaff);
        clinicalStaff = new ClinicalStaff();
        assertFalse(clinicalStaff.equals(anotherClinicalStaff));
        anotherClinicalStaff = new ClinicalStaff();
        assertEquals(anotherClinicalStaff, clinicalStaff);
        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());

        clinicalStaff.setFirstName("john");
        assertFalse(clinicalStaff.equals(anotherClinicalStaff));
        anotherClinicalStaff.setFirstName("john");
        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());
        assertEquals(anotherClinicalStaff, clinicalStaff);

        clinicalStaff.setLastName("dow");
        assertFalse(clinicalStaff.equals(anotherClinicalStaff));
        anotherClinicalStaff.setLastName("dow");
        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());
        assertEquals(anotherClinicalStaff, clinicalStaff);

        clinicalStaff.setMiddleName("mid");
        assertFalse(clinicalStaff.equals(anotherClinicalStaff));
        anotherClinicalStaff.setMiddleName("mid");
        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());
        assertEquals(anotherClinicalStaff, clinicalStaff);

        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());
        assertEquals(anotherClinicalStaff, clinicalStaff);

        clinicalStaff.setEmailAddress("a@a.com");
        assertFalse(clinicalStaff.equals(anotherClinicalStaff));
        anotherClinicalStaff.setEmailAddress("a@a.com");
        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());
        assertEquals(anotherClinicalStaff, clinicalStaff);

        clinicalStaff.setFaxNumber("123 345");
        assertFalse(clinicalStaff.equals(anotherClinicalStaff));
        anotherClinicalStaff.setFaxNumber("123 345");
        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());
        assertEquals(anotherClinicalStaff, clinicalStaff);

        clinicalStaff.setNciIdentifier("NCI 1");
        assertFalse(clinicalStaff.equals(anotherClinicalStaff));
        anotherClinicalStaff.setNciIdentifier("NCI 1");
        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());
        assertEquals(anotherClinicalStaff, clinicalStaff);

        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());
        assertEquals(anotherClinicalStaff, clinicalStaff);

        clinicalStaff.setPhoneNumber("123 123");
        assertFalse(clinicalStaff.equals(anotherClinicalStaff));
        anotherClinicalStaff.setPhoneNumber("123 123");
        assertEquals(anotherClinicalStaff.hashCode(), clinicalStaff.hashCode());
        assertEquals(anotherClinicalStaff, clinicalStaff);


    }
}
