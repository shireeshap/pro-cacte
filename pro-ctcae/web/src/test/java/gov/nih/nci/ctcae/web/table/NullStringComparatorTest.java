package gov.nih.nci.ctcae.web.table;

import gov.nih.nci.ctcae.core.AbstractTestCase;

import java.util.Comparator;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class NullStringComparatorTest extends AbstractTestCase {

    private NullStringComparator nullStringComparator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        nullStringComparator = new NullStringComparator();

    }

    public void testConstructor() {
        Comparator<String> stringComparator = nullStringComparator.getStringComparator();
        assertEquals("must be case insensitive", String.CASE_INSENSITIVE_ORDER, stringComparator);

    }
}
