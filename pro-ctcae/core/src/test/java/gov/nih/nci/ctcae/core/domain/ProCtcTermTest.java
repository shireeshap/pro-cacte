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
        assertNull(proCtcTerm.getCategory());
        assertNull(proCtcTerm.getCtepCode());
        assertNull(proCtcTerm.getCtepTerm());

    }

    public void testGetterAndSetter() {
        proCtcTerm = new ProCtcTerm();
        proCtcTerm.setCategory(category);
        proCtcTerm.setCtepCode("ctep code");
        proCtcTerm.setCtepTerm("ctep term");
        proCtcTerm.setProCtc(proCtc);
        proCtcTerm.setSelect("select");
        proCtcTerm.setTerm("term");
        assertEquals(category, proCtcTerm.getCategory());
        assertEquals("ctep code", proCtcTerm.getCtepCode());
        assertEquals("ctep term", proCtcTerm.getCtepTerm());
        assertEquals(proCtc, proCtcTerm.getProCtc());
        assertEquals("select", proCtcTerm.getSelect());
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

        proCtcTerm.setCategory(category);
        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        anotherProCtcTerm.setCategory(category);
        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

        proCtcTerm.setCtepCode("ctep code");
        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        anotherProCtcTerm.setCtepCode("ctep code");
        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

        proCtcTerm.setCtepTerm("ctep term");
        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        anotherProCtcTerm.setCtepTerm("ctep term");
        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

        proCtcTerm.setProCtc(proCtc);
        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        anotherProCtcTerm.setProCtc(proCtc);
        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

        proCtcTerm.setSelect("select");
        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        anotherProCtcTerm.setSelect("select");
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

        proCtcTerm.setCategory(category);
        anotherProCtcTerm.setCategory(category);
        anotherProCtcTerm.setId(2);
        assertEquals("must not consider id", anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

    }

    public void testEqualsAndHashCodeMustNotConsiderProCtcQuestions() {
        ProCtcTerm anotherProCtcTerm = new ProCtcTerm();
        proCtcTerm = new ProCtcTerm();

        proCtcTerm.setCategory(category);
        anotherProCtcTerm.setCategory(category);
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