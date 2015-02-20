package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */
public class StudyOrganizationTest extends TestCase {
    private Study study;


    private StudySite studySite;
    private Organization organization;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        study = new Study();


        organization = new Organization();
    }

    public void testEqualsAndHashCode() {

        StudySite anotherStudySite = null;

        assertEquals(anotherStudySite, studySite);
        studySite = new StudySite();
        assertFalse(studySite.equals(anotherStudySite));

        anotherStudySite = new StudySite();
        assertEquals(anotherStudySite, studySite);
        assertEquals(anotherStudySite.hashCode(), studySite.hashCode());

        studySite.setStudy(study);
        assertFalse(anotherStudySite.equals(studySite));
        anotherStudySite.setStudy(study);
        assertEquals(anotherStudySite, studySite);
        assertEquals(anotherStudySite.hashCode(), studySite.hashCode());

        studySite.setOrganization(organization);
        assertFalse(anotherStudySite.equals(studySite));
        anotherStudySite.setOrganization(organization);
        assertEquals(anotherStudySite, studySite);
        assertEquals(anotherStudySite.hashCode(), studySite.hashCode());

    }

    public void testEqualsAndHashCodeMustNotConsiderId() {

        StudySite anotherStudySite = new StudySite();
        studySite = new StudySite();

        anotherStudySite.setId(1);
        studySite.setId(1);
        assertEquals("must not consider id", anotherStudySite, studySite);
        assertEquals("must not consider id", anotherStudySite.hashCode(), studySite.hashCode());


    }

}