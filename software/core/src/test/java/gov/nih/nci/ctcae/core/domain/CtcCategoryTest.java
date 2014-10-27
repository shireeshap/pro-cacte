package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @created Nov 13, 2008
 */
public class CtcCategoryTest extends TestCase {
    private CtcCategory ctcCategory;

    public void testConstructor() {
        ctcCategory = new CtcCategory();
        assertNull(ctcCategory.getName());
        assertNull(ctcCategory.getId());
    }

    public void testGetterAndSetter() {
        ctcCategory = new CtcCategory();
        ctcCategory.setName("name");
        assertEquals("name", ctcCategory.getName());
    }

    public void testEqualsAndHashCode() {
        CtcCategory anotherCtcCategory = null;
        assertEquals(anotherCtcCategory, ctcCategory);
        ctcCategory = new CtcCategory();
        assertFalse(ctcCategory.equals(anotherCtcCategory));
        anotherCtcCategory = new CtcCategory();
        assertEquals(anotherCtcCategory, ctcCategory);
        assertEquals(anotherCtcCategory.hashCode(), ctcCategory.hashCode());

        ctcCategory.setName("name");
        assertFalse(ctcCategory.equals(anotherCtcCategory));

        anotherCtcCategory.setName("name");
        assertEquals(anotherCtcCategory.hashCode(), ctcCategory.hashCode());
        assertEquals(anotherCtcCategory, ctcCategory);


    }

    public void testEqualsAndHashCodeMustMNotConsiderId() {
        CtcCategory anotherCtcCategory = new CtcCategory();
        ctcCategory = new CtcCategory();

        ctcCategory.setName("name");
        anotherCtcCategory.setName("name");

        ctcCategory.setId(1);
        assertEquals("must not consider id", anotherCtcCategory, ctcCategory);
        assertEquals(anotherCtcCategory.hashCode(), ctcCategory.hashCode());

    }


}