package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @created Nov 17, 2008
 */
public class CtcTermTest extends TestCase {
    private CtcTerm ctcTerm;
    private ProCtc proCtc;
    private CtcCategory category;

    public void testConstructor() {
        ctcTerm = new CtcTerm();
        assertNull(ctcTerm.getCtepCode());
        assertNull(ctcTerm.getCtepTerm());

    }

    public void testGetterAndSetter() {
        ctcTerm = new CtcTerm();
        ctcTerm.setCtepCode("ctep code");
        ctcTerm.setCtepTerm("ctep term");
        ctcTerm.setTerm("term", SupportedLanguageEnum.ENGLISH);
        assertEquals("ctep code", ctcTerm.getCtepCode());
        assertEquals("ctep term", ctcTerm.getCtepTerm());
        assertEquals("term", ctcTerm.getTerm(SupportedLanguageEnum.ENGLISH));

    }

    public void testEqualsAndHashCode() {
        CtcTerm anotherCtcTerm = null;
        assertEquals(anotherCtcTerm, ctcTerm);
        ctcTerm = new CtcTerm();
        assertFalse(ctcTerm.equals(anotherCtcTerm));
        anotherCtcTerm = new CtcTerm();
        assertEquals(anotherCtcTerm, ctcTerm);
        assertEquals(anotherCtcTerm.hashCode(), ctcTerm.hashCode());
        assertTrue(ctcTerm.equals(anotherCtcTerm)); // need to make it assertTrue for the test to pass

        ctcTerm.setCtepCode("ctep code");
        assertFalse(ctcTerm.equals(anotherCtcTerm));
        anotherCtcTerm.setCtepCode("ctep code");
        assertEquals(anotherCtcTerm.hashCode(), ctcTerm.hashCode());
        assertEquals(anotherCtcTerm, ctcTerm);

        ctcTerm.setCtepTerm("ctep term");
        assertFalse(ctcTerm.equals(anotherCtcTerm));
        anotherCtcTerm.setCtepTerm("ctep term");
        assertEquals(anotherCtcTerm.hashCode(), ctcTerm.hashCode());
        assertEquals(anotherCtcTerm, ctcTerm);

        ctcTerm.setSelect("select");
        assertFalse(ctcTerm.equals(anotherCtcTerm));
        anotherCtcTerm.setSelect("select");
        assertEquals(anotherCtcTerm.hashCode(), ctcTerm.hashCode());
        assertEquals(anotherCtcTerm, ctcTerm);

        ctcTerm.setTerm("term", SupportedLanguageEnum.ENGLISH);
        assertFalse(ctcTerm.equals(anotherCtcTerm));
        anotherCtcTerm.setTerm("term", SupportedLanguageEnum.ENGLISH);
        assertEquals(anotherCtcTerm.hashCode(), ctcTerm.hashCode());
        assertEquals(anotherCtcTerm, ctcTerm);


    }


    protected void setUp() throws Exception {
        super.setUp();
        proCtc = new ProCtc();
        category = new CtcCategory();
    }
}