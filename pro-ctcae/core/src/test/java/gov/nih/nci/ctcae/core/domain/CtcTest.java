package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author mehul gulati
 * Date: Jan 20, 2009
 */
public class CtcTest extends TestCase {
    private Ctc ctc;

    public void testConstructor(){
        ctc = new Ctc();
        assertNull(ctc.getName());
    }

    public void testGetterAndSetter(){
        ctc = new Ctc();
        ctc.setName("abc");

        assertEquals("abc", ctc.getName());
    }

    public void testEqualsAndHashCode(){
        Ctc anotherCtc = null;
        assertEquals(anotherCtc, ctc);
        ctc = new Ctc();
        assertFalse(ctc.equals(anotherCtc));
        anotherCtc = new Ctc();
        assertEquals(ctc, anotherCtc);
        assertEquals(anotherCtc.hashCode(), ctc.hashCode());

        ctc.setName("abc");
        assertFalse(ctc.equals(anotherCtc));
        anotherCtc.setName("abc");
        assertEquals(anotherCtc.hashCode(), ctc.hashCode());
        assertEquals(anotherCtc, ctc);
    }

    public void testEqualsAndHashCodeMustNotConsiderId(){
        Ctc anotherCtc = new Ctc();
        ctc = new Ctc();

        anotherCtc.setId(1);
        assertEquals("must not consider id", anotherCtc.hashCode(), ctc.hashCode());
        assertEquals(anotherCtc, ctc);
    }

    public void testEqualsAndHashCodeMustNotConsiderCategory() {
        Ctc anotherCtc = new Ctc();
        ctc = new Ctc();

        anotherCtc.getCtcCategories().add(new CtcCategory());
        assertEquals("must not consider category", anotherCtc.hashCode(), ctc.hashCode());
        assertEquals(anotherCtc, ctc);


    }
}
