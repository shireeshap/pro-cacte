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
        ProCtcTermVocab proCtcTermVocab = new ProCtcTermVocab(proCtcTerm);
        proCtcTerm.setProCtcTermVocab(proCtcTermVocab);
        proCtcTerm.getProCtcTermVocab().setTermEnglish("term");
        assertEquals(proCtc, proCtcTerm.getProCtc());
        assertEquals("term", proCtcTerm.getProCtcTermVocab().getTermEnglish());

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

        ProCtcTermVocab proCtcTermVocab = new ProCtcTermVocab(proCtcTerm);
        proCtcTerm.setProCtcTermVocab(proCtcTermVocab);
        proCtcTerm.getProCtcTermVocab().setTermEnglish("term");
        assertFalse(proCtcTerm.equals(anotherProCtcTerm));
        
        ProCtcTermVocab proCtcTermVocab2 = new ProCtcTermVocab();
        anotherProCtcTerm.setProCtcTermVocab(proCtcTermVocab2);
        anotherProCtcTerm.getProCtcTermVocab().setTermEnglish("term");
        assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
        assertEquals(anotherProCtcTerm, proCtcTerm);

    }




    protected void setUp() throws Exception {
        super.setUp();
        proCtc = new ProCtc();
        category = new CtcCategory();
    }
}