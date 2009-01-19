package gov.nih.nci.ctcae.core.domain;

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
        assertNull(ctcTerm.getCategory());
        assertNull(ctcTerm.getCtepCode());
        assertNull(ctcTerm.getCtepTerm());

    }

    public void testGetterAndSetter() {
        ctcTerm = new CtcTerm();
        ctcTerm.setCategory(category);
        ctcTerm.setCtepCode("ctep code");
        ctcTerm.setCtepTerm("ctep term");
        ctcTerm.setTerm("term");
        assertEquals(category, ctcTerm.getCategory());
        assertEquals("ctep code", ctcTerm.getCtepCode());
        assertEquals("ctep term", ctcTerm.getCtepTerm());
        assertEquals("term", ctcTerm.getTerm());

    }

    public void testEqualsAndHashCode() {
        CtcTerm anotherCtcTerm = null;
        assertEquals(anotherCtcTerm, ctcTerm);
        ctcTerm = new CtcTerm();
        assertFalse(ctcTerm.equals(anotherCtcTerm));
        anotherCtcTerm = new CtcTerm();
        assertEquals(anotherCtcTerm, ctcTerm);
        assertEquals(anotherCtcTerm.hashCode(), ctcTerm.hashCode());

        ctcTerm.setCategory(category);
        assertFalse(ctcTerm.equals(anotherCtcTerm));
        anotherCtcTerm.setCategory(category);
        assertEquals(anotherCtcTerm.hashCode(), ctcTerm.hashCode());
        assertEquals(anotherCtcTerm, ctcTerm);

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

        ctcTerm.setTerm("term");
        assertFalse(ctcTerm.equals(anotherCtcTerm));
        anotherCtcTerm.setTerm("term");
        assertEquals(anotherCtcTerm.hashCode(), ctcTerm.hashCode());
        assertEquals(anotherCtcTerm, ctcTerm);


    }

    public void testEqualsAndHashCodeMustNotConsiderId() {
        CtcTerm anotherCtcTerm = new CtcTerm();

        ctcTerm = new CtcTerm();
        ctcTerm.setCategory(category);
        anotherCtcTerm.setCategory(category);
        anotherCtcTerm.setId(2);
        assertEquals("must not consider id", anotherCtcTerm.hashCode(), ctcTerm.hashCode());
        assertEquals(anotherCtcTerm, ctcTerm);

    }

    public void testEqualsAndHashCodeMustNotConsiderProCtcQuestions() {
        CtcTerm anotherCtcTerm = new CtcTerm();
        ctcTerm = new CtcTerm();

        ctcTerm.setCategory(category);
        anotherCtcTerm.setCategory(category);
        assertEquals("must not consider pro ctc questions", anotherCtcTerm.hashCode(), ctcTerm.hashCode());
        assertEquals(anotherCtcTerm, ctcTerm);

    }


    protected void setUp() throws Exception {
        super.setUp();
        proCtc = new ProCtc();
        category = new CtcCategory();
    }
}