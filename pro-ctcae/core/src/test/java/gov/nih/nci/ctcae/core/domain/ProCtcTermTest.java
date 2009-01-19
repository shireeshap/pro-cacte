package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @created Nov 17, 2008
 */
public class ProCtcTermTest extends TestCase {
    private ProCtcTerm proCtcTerm;
    private ProCtc proCtc;
    private CtcCategory category;

    public void testConstructor() {
        proCtcTerm = new ProCtcTerm();
        assertNotNull(proCtcTerm.getProCtcQuestions());
        assertTrue(proCtcTerm.getProCtcQuestions().isEmpty());

    }

    public void testGetterAndSetter() {
        proCtcTerm = new ProCtcTerm();
        proCtcTerm.setProCtc(proCtc);
        proCtcTerm.setTerm("term");
        assertEquals(proCtc, proCtcTerm.getProCtc());
        assertEquals("term", proCtcTerm.getTerm());

    }

    public void testEqualsAndHashCode() {
        ProCtcTerm anotherProCtcTerm = null;
        assertEquals(anotherProCtcTerm, proCtcTerm);
        proCtcTerm = new ProCtcTerm();
        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        anotherProCtcTerm = new ProCtcTerm();
        assertEquals(anotherProCtcTerm, proCtcTerm);
        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());

        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

        proCtcTerm.setProCtc(proCtc);
        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        anotherProCtcTerm.setProCtc(proCtc);
        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

//        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

        proCtcTerm.setTerm("term");
        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        anotherProCtcTerm.setTerm("term");
        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);


    }

    public void testEqualsAndHashCodeMustNotConsiderId() {
        ProCtcTerm anotherProCtcTerm = new ProCtcTerm();

        proCtcTerm = new ProCtcTerm();

        anotherProCtcTerm.setId(2);
        assertEquals("must not consider id", anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

    }

    public void testEqualsAndHashCodeMustNotConsiderProCtcQuestions() {
        ProCtcTerm anotherProCtcTerm = new ProCtcTerm();
        proCtcTerm = new ProCtcTerm();

        anotherProCtcTerm.getProCtcQuestions().add(new ProCtcQuestion());
        assertEquals("must not consider pro ctc questions", anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

    }


    protected void setUp() throws Exception {
        super.setUp();
        proCtc = new ProCtc();
        category = new CtcCategory();
    }
}