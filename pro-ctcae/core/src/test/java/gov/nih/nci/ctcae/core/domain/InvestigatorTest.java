package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author mehul
 */

public class InvestigatorTest extends TestCase {
    private Investigator investigator;

    public void testConstructor() {
        investigator = new Investigator();
        assertNull(investigator.getFirstName());
        assertNull(investigator.getLastName());
    }

    public void testGetterAndSetter() {
        investigator = new Investigator();
        investigator.setFirstName("john");
        investigator.setLastName("dow");
        investigator.setMiddleName("mid");
        investigator.setAddress("abc");
        investigator.setEmailAddress("a@a.com");
        investigator.setFaxNumber("123 345");
        investigator.setNciIdentifier("NCI 1");
        investigator.setPhoneNumber("123 123");
        investigator.setTitle("mr");

        assertEquals("john", investigator.getFirstName());
        assertEquals("dow", investigator.getLastName());
        assertEquals("mid", investigator.getMiddleName());
        assertEquals("abc", investigator.getAddress());
        assertEquals("a@a.com", investigator.getEmailAddress());
        assertEquals("123 345", investigator.getFaxNumber());
        assertEquals("NCI 1", investigator.getNciIdentifier());
        assertEquals("mr", investigator.getTitle());
        assertEquals("123 123", investigator.getPhoneNumber());
    }

    public void testEqualsAndHashCode() {
        Investigator anotherInvestigator = null;
        assertEquals(anotherInvestigator, investigator);
        investigator = new Investigator();
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator = new Investigator();
        assertEquals(anotherInvestigator, investigator);
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());

        investigator.setFirstName("john");
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator.setFirstName("john");
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());
        assertEquals(anotherInvestigator, investigator);

        investigator.setLastName("dow");
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator.setLastName("dow");
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());
        assertEquals(anotherInvestigator, investigator);

        investigator.setMiddleName("mid");
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator.setMiddleName("mid");
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());
        assertEquals(anotherInvestigator, investigator);

        investigator.setAddress("abc");
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator.setAddress("abc");
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());
        assertEquals(anotherInvestigator, investigator);

        investigator.setEmailAddress("a@a.com");
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator.setEmailAddress("a@a.com");
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());
        assertEquals(anotherInvestigator, investigator);

        investigator.setFaxNumber("123 345");
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator.setFaxNumber("123 345");
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());
        assertEquals(anotherInvestigator, investigator);

        investigator.setNciIdentifier("NCI 1");
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator.setNciIdentifier("NCI 1");
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());
        assertEquals(anotherInvestigator, investigator);

        investigator.setTitle("mr");
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator.setTitle("mr");
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());
        assertEquals(anotherInvestigator, investigator);

        investigator.setPhoneNumber("123 123");
        assertFalse(investigator.equals(anotherInvestigator));
        anotherInvestigator.setPhoneNumber("123 123");
        assertEquals(anotherInvestigator.hashCode(), investigator.hashCode());
        assertEquals(anotherInvestigator, investigator);


    }
}
