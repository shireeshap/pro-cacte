package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Mehul Gulati
 * Date: Oct 27, 2008
 */
public class ResearchStaffTest extends TestCase {
    private ResearchStaff researchStaff;

    public void testConstructor() {
        researchStaff = new ResearchStaff();
        assertNull(researchStaff.getFirstName());
        assertNull(researchStaff.getLastName());
    }

    public void testGetterAndSetter(){
        researchStaff = new ResearchStaff();
        researchStaff.setFirstName("john");
        researchStaff.setLastName("dow");
        researchStaff.setMiddleName("mid");
        researchStaff.setAddress("abc");
        researchStaff.setEmailAddress("a@a.com");
        researchStaff.setFaxNumber("123 345");
        researchStaff.setPhoneNumber("123 345");
        researchStaff.setTitle("mr");
        researchStaff.setResearcherID("ID 5");
       
        assertEquals("john", researchStaff.getFirstName());
        assertEquals("dow", researchStaff.getLastName());
        assertEquals("mid", researchStaff.getMiddleName());
        assertEquals("abc", researchStaff.getAddress());
        assertEquals("a@a.com", researchStaff.getEmailAddress());
        assertEquals("123 345", researchStaff.getFaxNumber());
        assertEquals("123 345", researchStaff.getPhoneNumber());
        assertEquals("mr", researchStaff.getTitle());
        assertEquals("ID 5", researchStaff.getResearcherID());
    }

    public  void testEqualsAndHashCode() {
        ResearchStaff anotherResearchStaff = null;
        assertEquals(anotherResearchStaff, researchStaff);
        researchStaff = new ResearchStaff();
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff = new ResearchStaff();
        assertEquals(anotherResearchStaff, researchStaff);
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());

        researchStaff.setFirstName("john");
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff.setFirstName("john");
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());
        assertEquals(anotherResearchStaff, researchStaff);

        researchStaff.setLastName("dow");
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff.setLastName("dow");
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());
        assertEquals(anotherResearchStaff, researchStaff);

        researchStaff.setMiddleName("mid");
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff.setMiddleName("mid");
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());
        assertEquals(anotherResearchStaff, researchStaff);

        researchStaff.setAddress("abc");
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff.setAddress("abc");
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());
        assertEquals(anotherResearchStaff, researchStaff);

        researchStaff.setEmailAddress("a@a.com");
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff.setEmailAddress("a@a.com");
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());
        assertEquals(anotherResearchStaff, researchStaff);

        researchStaff.setFaxNumber("123 345");
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff.setFaxNumber("123 345");
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());
        assertEquals(anotherResearchStaff, researchStaff);

        researchStaff.setResearcherID("ID 5");
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff.setResearcherID("ID 5");
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());
        assertEquals(anotherResearchStaff, researchStaff);

        researchStaff.setTitle("mr");
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff.setTitle("mr");
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());
        assertEquals(anotherResearchStaff, researchStaff);

        researchStaff.setPhoneNumber("123 345");
        assertFalse(researchStaff.equals(anotherResearchStaff));
        anotherResearchStaff.setPhoneNumber("123 345");
        assertEquals(anotherResearchStaff.hashCode(), researchStaff.hashCode());
        assertEquals(anotherResearchStaff, researchStaff);


    }
}
