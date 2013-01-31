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

    public void testProCtcTermVocabEqualsAndHashcode(){
    	String termEnglishText1 = "Aching muscles";
    	String termEnglishText2 = "Decreased appetite";
    	String termSpanishText1 = "Dolor muscular";
    	String termSpanishText2 = "Disminución del apetito";
    	
    	ProCtcTerm proCtcTerm1 = new ProCtcTerm();
    	ProCtcTermVocab proCtcTermVocab1 = new ProCtcTermVocab();
    	proCtcTermVocab1.setTermEnglish(termEnglishText1);
    	proCtcTermVocab1.setTermSpanish(termSpanishText1);
    	proCtcTerm1.setProCtcTermVocab(proCtcTermVocab1);
    	
    	ProCtcTerm proCtcTerm2 = new ProCtcTerm();
    	ProCtcTermVocab proCtcTermVocab2 = new ProCtcTermVocab();
    	proCtcTermVocab2.setTermEnglish(termEnglishText2);
    	proCtcTermVocab2.setTermSpanish(termSpanishText2);
    	proCtcTerm2.setProCtcTermVocab(proCtcTermVocab2);
    	
    	assertTrue(proCtcTerm1.getProCtcTermVocab().equals(proCtcTermVocab1));
    	assertTrue(proCtcTerm2.getProCtcTermVocab().equals(proCtcTermVocab2));
    	// If proCtcTermVocab1 & proCtcTermVocab2 have different English and
		// different Spanish Text, expect equals() and hashcode() to return false 
    	assertFalse(proCtcTerm1.getProCtcTermVocab().equals(proCtcTerm2.getProCtcTermVocab()));
    	assertNotSame(proCtcTerm1.getProCtcTermVocab().hashCode(),proCtcTerm2.getProCtcTermVocab().hashCode());
    	
		// If proCtcTermVocab1 & proCtcTermVocab2 have same English text but
		// different Spanish Text, expect equals() and hashcode() to return true 
    	proCtcTermVocab2.setTermEnglish(termEnglishText1);
    	assertTrue(proCtcTerm1.getProCtcTermVocab().equals(proCtcTerm2.getProCtcTermVocab()));
    	assertEquals(proCtcTerm1.getProCtcTermVocab().hashCode(),proCtcTerm2.getProCtcTermVocab().hashCode());
    	
    	assertEquals(proCtcTerm1.getProCtcTermVocab().toString(), termEnglishText1);
    	assertNotSame(proCtcTerm1.getProCtcTermVocab().toString(), termSpanishText1);
    	
    }



    protected void setUp() throws Exception {
        super.setUp();
        proCtc = new ProCtc();
        category = new CtcCategory();
    }
}