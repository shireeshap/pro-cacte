package gov.nih.nci.ctcae.web.table;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.TestBean;

import java.util.Comparator;

/**
 * @author Vinay Kumar
 * @since Nov 4, 2008
 */
public class NullStringComparatorTest extends AbstractTestCase {

	private NullStringComparator comparator;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		comparator = new NullStringComparator();

	}

	public void testConstructor() {
		Comparator<String> stringComparator = comparator.getStringComparator();
		assertEquals("must be case insensitive", String.CASE_INSENSITIVE_ORDER, stringComparator);

	}

	public void testCompare() {
		assertEquals("must be case insensitive", 0, comparator.compare(null, null));
		assertEquals("must be case insensitive", -1, comparator.compare("", null));
		assertEquals("must be case insensitive", -1, comparator.compare(new TestBean(), null));
		assertEquals("must be case insensitive", 1, comparator.compare(null, new TestBean()));

	}
}
