package gov.nih.nci.ctcae.core.domain;

import static gov.nih.nci.ctcae.core.Fixture.createCrfItemDisplayRules;
import junit.framework.TestCase;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class CrfItemTest extends TestCase {
	private CrfPageItem crfPageItem;

	private CrfItemDisplayRule crfItemDisplayRule1, crfItemDisplayRule2, crfItemDisplayRule3, crfItemDisplayRule4, crfItemDisplayRule5;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		crfPageItem = new CrfPageItem();
		crfItemDisplayRule1 = createCrfItemDisplayRules(1, 1);
		crfItemDisplayRule2 = createCrfItemDisplayRules(2, 2);
		crfItemDisplayRule3 = createCrfItemDisplayRules(3, 3);
		crfItemDisplayRule4 = createCrfItemDisplayRules(4, 4);
		crfItemDisplayRule5 = createCrfItemDisplayRules(5, 5);


	}


	public void testConstructor() {
		assertEquals(Integer.valueOf(0), crfPageItem.getDisplayOrder());
		assertFalse("must not require response", crfPageItem.getResponseRequired());
	}

	public void testGetterAndSetter() {
		crfPageItem.setDisplayOrder(1);
		crfPageItem.setResponseRequired(Boolean.TRUE);
		crfPageItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
		crfPageItem.setInstructions("instructions");

		assertEquals(Integer.valueOf(1), crfPageItem.getDisplayOrder());
		assertEquals(CrfItemAllignment.HORIZONTAL, crfPageItem.getCrfItemAllignment());
		assertEquals("instructions", crfPageItem.getInstructions());
		assertTrue(crfPageItem.getResponseRequired());

	}

	public void testEqualsAndHashCode() {

		crfPageItem = null;
		CrfPageItem anothercrfPageItem = null;
		assertEquals(anothercrfPageItem, crfPageItem);

		crfPageItem = new CrfPageItem();
		assertFalse(crfPageItem.equals(anothercrfPageItem));

		anothercrfPageItem = new CrfPageItem();
		assertEquals(anothercrfPageItem, crfPageItem);
		assertEquals(anothercrfPageItem.hashCode(), crfPageItem.hashCode());

		crfPageItem.setDisplayOrder(1);
		assertFalse(crfPageItem.equals(anothercrfPageItem));
		anothercrfPageItem.setDisplayOrder(1);
		assertEquals(anothercrfPageItem.hashCode(), crfPageItem.hashCode());
		assertEquals(anothercrfPageItem, crfPageItem);

		CRFPage crfPage = new CRFPage();
		crfPageItem.setCrfPage(crfPage);
		assertFalse(crfPageItem.equals(anothercrfPageItem));
		anothercrfPageItem.setCrfPage(crfPage);
		assertEquals(anothercrfPageItem.hashCode(), crfPageItem.hashCode());
		assertEquals(anothercrfPageItem, crfPageItem);

		ProCtcQuestion proCtcQuestion = new ProCtcQuestion();
		crfPageItem.setProCtcQuestion(proCtcQuestion);
		assertFalse(crfPageItem.equals(anothercrfPageItem));
		anothercrfPageItem.setProCtcQuestion(proCtcQuestion);
		assertEquals(anothercrfPageItem.hashCode(), crfPageItem.hashCode());
		assertEquals(anothercrfPageItem, crfPageItem);

		crfPageItem.setResponseRequired(Boolean.TRUE);
		assertFalse(crfPageItem.equals(anothercrfPageItem));
		anothercrfPageItem.setResponseRequired(Boolean.TRUE);
		assertEquals(anothercrfPageItem.hashCode(), crfPageItem.hashCode());
		assertEquals(anothercrfPageItem, crfPageItem);

		crfPageItem.setInstructions("inst");
		assertFalse(crfPageItem.equals(anothercrfPageItem));
		anothercrfPageItem.setInstructions("inst");
		assertEquals(anothercrfPageItem.hashCode(), crfPageItem.hashCode());
		assertEquals(anothercrfPageItem, crfPageItem);

		crfPageItem.setCrfItemAllignment(CrfItemAllignment.VERTICAL);
		assertFalse(crfPageItem.equals(anothercrfPageItem));
		anothercrfPageItem.setCrfItemAllignment(CrfItemAllignment.VERTICAL);
		assertEquals(anothercrfPageItem.hashCode(), crfPageItem.hashCode());
		assertEquals(anothercrfPageItem, crfPageItem);

	}

	public void testEqualsAndHashCodeMustNotConsiderId() {

		CrfPageItem anothercrfPageItem = null;

		crfPageItem = new CrfPageItem();

		anothercrfPageItem = new CrfPageItem();

		crfPageItem.setDisplayOrder(1);
		anothercrfPageItem.setDisplayOrder(1);
		anothercrfPageItem.setId(1);
		assertEquals("must not consider id", anothercrfPageItem.hashCode(), crfPageItem.hashCode());
		assertEquals(anothercrfPageItem, crfPageItem);

	}


	public void testRemoveCrfItemDisplayRules() {

		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule1);
		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule2);
		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule3);
		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule4);
		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule5);
		assertEquals("must be 5 rules", 5, crfPageItem.getCrfItemDisplayRules().size());

		crfPageItem.removeCrfItemDisplayRulesByIds("1,3,2");

		assertEquals("must remove 3 rules", 2, crfPageItem.getCrfItemDisplayRules().size());

		assertTrue("must preseve the order of rules", crfPageItem.getCrfItemDisplayRules().contains(crfItemDisplayRule4));
		assertTrue("must preseve the order of  rules", crfPageItem.getCrfItemDisplayRules().contains(crfItemDisplayRule5));

	}

	public void testAddCrfItemDisplayRules() {
		assertTrue("must not have any display rules", crfPageItem.getCrfItemDisplayRules().isEmpty());
		CrfItemDisplayRule nullCrfItemDisplayRule = null;
		crfPageItem.addCrfItemDisplayRules(nullCrfItemDisplayRule);
		assertTrue("must not add null display rules", crfPageItem.getCrfItemDisplayRules().isEmpty());

		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule1);
		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule2);
		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule3);
		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule4);
		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule5);
		assertEquals("must be 5 rules", 5, crfPageItem.getCrfItemDisplayRules().size());

		for (CrfItemDisplayRule crfItemDisplayRule : crfPageItem.getCrfItemDisplayRules()) {
			assertEquals("crf item must not be null", crfPageItem, crfItemDisplayRule.getCrfItem());
		}


	}

	public void testMustNotAddSameCrfItemDisplayRule() {
		assertTrue("must not have any display rules", crfPageItem.getCrfItemDisplayRules().isEmpty());
		CrfItemDisplayRule nullCrfItemDisplayRule = null;
		crfPageItem.addCrfItemDisplayRules(nullCrfItemDisplayRule);
		assertTrue("must not add null display rules", crfPageItem.getCrfItemDisplayRules().isEmpty());

		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule1);
		assertEquals("must be 1 rules", 1, crfPageItem.getCrfItemDisplayRules().size());

		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule1);

		crfPageItem.addCrfItemDisplayRules(crfItemDisplayRule5);
		assertEquals("must be 2 rules only because same rule must not be added twice", 2, crfPageItem.getCrfItemDisplayRules().size());

		for (CrfItemDisplayRule crfItemDisplayRule : crfPageItem.getCrfItemDisplayRules()) {
			assertEquals("crf item must not be null", crfPageItem, crfItemDisplayRule.getCrfItem());
		}


	}

}