package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class CrfItemTest extends TestCase {
    private CrfItem crfItem;

    public void testConstructor() {
        crfItem = new CrfItem();
        assertEquals(Integer.valueOf(0), crfItem.getDisplayOrder());
        assertFalse("must not require response", crfItem.getResponseRequired());
    }

    public void testGetterAndSetter() {
        crfItem = new CrfItem();
        crfItem.setDisplayOrder(1);
        crfItem.setResponseRequired(Boolean.TRUE);
        crfItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
        crfItem.setInstructions("instructions");

        assertEquals(Integer.valueOf(1), crfItem.getDisplayOrder());
        assertEquals(CrfItemAllignment.HORIZONTAL, crfItem.getCrfItemAllignment());
        assertEquals("instructions", crfItem.getInstructions());
        assertTrue(crfItem.getResponseRequired());

    }

    public void testEqualsAndHashCode() {

        CrfItem anothercrfItem = null;
        assertEquals(anothercrfItem, crfItem);

        crfItem = new CrfItem();
        assertFalse(crfItem.equals(anothercrfItem));

        anothercrfItem = new CrfItem();
        assertEquals(anothercrfItem, crfItem);
        assertEquals(anothercrfItem.hashCode(), crfItem.hashCode());

        crfItem.setDisplayOrder(1);
        assertFalse(crfItem.equals(anothercrfItem));

        anothercrfItem.setDisplayOrder(1);
        assertEquals(anothercrfItem.hashCode(), crfItem.hashCode());
        assertEquals(anothercrfItem, crfItem);

    }

    public void testEqualsAndHashCodeMustNotConsiderId() {

        CrfItem anothercrfItem = null;

        crfItem = new CrfItem();

        anothercrfItem = new CrfItem();

        crfItem.setDisplayOrder(1);
        anothercrfItem.setDisplayOrder(1);
        anothercrfItem.setId(1);
        assertEquals("must not consider id", anothercrfItem.hashCode(), crfItem.hashCode());
        assertEquals(anothercrfItem, crfItem);

    }

}