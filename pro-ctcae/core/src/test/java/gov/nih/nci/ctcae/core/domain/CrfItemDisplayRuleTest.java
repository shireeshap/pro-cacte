package gov.nih.nci.ctcae.core.domain;

import static gov.nih.nci.ctcae.core.Fixture.createCrfItemDisplayRules;
import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @created Dec 17, 2008
 */
public class CrfItemDisplayRuleTest extends TestCase {
	private CrfItem crfItem;

	private CrfItemDisplayRule crfItemDisplayRule;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		crfItem = new CrfItem();
	}


	public void testGetterAndSetter() {
		crfItemDisplayRule = createCrfItemDisplayRules(1, 2);

		assertEquals(Integer.valueOf(2), crfItemDisplayRule.getRequiredObjectId());
		assertEquals(Integer.valueOf(1), crfItemDisplayRule.getId());
		assertEquals(ProCtcValidValue.class.getName(), crfItemDisplayRule.getRequiredObjectClass());

	}

	public void testEqualsAndHashCode() {

		CrfItemDisplayRule anotherCrfItemDisplayRule = null;
		assertEquals(anotherCrfItemDisplayRule, crfItemDisplayRule);

		crfItemDisplayRule = new CrfItemDisplayRule();
		assertFalse(crfItemDisplayRule.equals(anotherCrfItemDisplayRule));

		anotherCrfItemDisplayRule = new CrfItemDisplayRule();
		assertEquals(anotherCrfItemDisplayRule, crfItemDisplayRule);
		assertEquals(anotherCrfItemDisplayRule.hashCode(), crfItemDisplayRule.hashCode());

		crfItemDisplayRule.setRequiredObjectClass(ProCtcValidValue.class);
		assertFalse(crfItemDisplayRule.equals(anotherCrfItemDisplayRule));
		anotherCrfItemDisplayRule.setRequiredObjectClass(ProCtcValidValue.class);
		assertEquals(anotherCrfItemDisplayRule.hashCode(), crfItemDisplayRule.hashCode());
		assertEquals(anotherCrfItemDisplayRule, crfItemDisplayRule);

		crfItemDisplayRule.setCrfItem(crfItem);
		assertFalse(crfItemDisplayRule.equals(anotherCrfItemDisplayRule));
		anotherCrfItemDisplayRule.setCrfItem(crfItem);
		assertEquals(anotherCrfItemDisplayRule.hashCode(), crfItemDisplayRule.hashCode());
		assertEquals(anotherCrfItemDisplayRule, crfItemDisplayRule);

		crfItemDisplayRule.setRequiredObjectId(2);

		assertFalse(crfItemDisplayRule.equals(anotherCrfItemDisplayRule));
		anotherCrfItemDisplayRule.setRequiredObjectId(2);
		assertEquals(anotherCrfItemDisplayRule.hashCode(), crfItemDisplayRule.hashCode());
		assertEquals(anotherCrfItemDisplayRule, crfItemDisplayRule);


	}

	public void testEqualsAndHashCodeMustNotConsiderId() {

		CrfItemDisplayRule anotherCrfItemDisplayRule = new CrfItemDisplayRule();
		crfItemDisplayRule = new CrfItemDisplayRule();
		crfItemDisplayRule.setId(2);
		anotherCrfItemDisplayRule.setId(1);
		assertEquals(anotherCrfItemDisplayRule, crfItemDisplayRule);
		assertEquals("must not consider id", crfItemDisplayRule.hashCode(), anotherCrfItemDisplayRule.hashCode());

	}


}