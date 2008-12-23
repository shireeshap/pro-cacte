package gov.nih.nci.ctcae.core.domain;

import static gov.nih.nci.ctcae.core.Fixture.createCrfItemDisplayRules;
import junit.framework.TestCase;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class CrfItemTest extends TestCase {
	private CrfItem crfItem;

	private CrfItemDisplayRule crfItemDisplayRule1, crfItemDisplayRule2, crfItemDisplayRule3, crfItemDisplayRule4, crfItemDisplayRule5;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		crfItem = new CrfItem();
		crfItemDisplayRule1 = createCrfItemDisplayRules(1, 1);
		crfItemDisplayRule2 = createCrfItemDisplayRules(2, 2);
		crfItemDisplayRule3 = createCrfItemDisplayRules(3, 3);
		crfItemDisplayRule4 = createCrfItemDisplayRules(4, 4);
		crfItemDisplayRule5 = createCrfItemDisplayRules(5, 5);


	}


	public void testConstructor() {
		assertEquals(Integer.valueOf(0), crfItem.getDisplayOrder());
		assertFalse("must not require response", crfItem.getResponseRequired());
	}

	public void testGetterAndSetter() {
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

		crfItem = null;
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

		CRF crf = new CRF();
		crfItem.setCrf(crf);
		assertFalse(crfItem.equals(anothercrfItem));
		anothercrfItem.setCrf(crf);
		assertEquals(anothercrfItem.hashCode(), crfItem.hashCode());
		assertEquals(anothercrfItem, crfItem);

		ProCtcQuestion proCtcQuestion = new ProCtcQuestion();
		crfItem.setProCtcQuestion(proCtcQuestion);
		assertFalse(crfItem.equals(anothercrfItem));
		anothercrfItem.setProCtcQuestion(proCtcQuestion);
		assertEquals(anothercrfItem.hashCode(), crfItem.hashCode());
		assertEquals(anothercrfItem, crfItem);

		crfItem.setResponseRequired(Boolean.TRUE);
		assertFalse(crfItem.equals(anothercrfItem));
		anothercrfItem.setResponseRequired(Boolean.TRUE);
		assertEquals(anothercrfItem.hashCode(), crfItem.hashCode());
		assertEquals(anothercrfItem, crfItem);

		crfItem.setInstructions("inst");
		assertFalse(crfItem.equals(anothercrfItem));
		anothercrfItem.setInstructions("inst");
		assertEquals(anothercrfItem.hashCode(), crfItem.hashCode());
		assertEquals(anothercrfItem, crfItem);

		crfItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
		assertFalse(crfItem.equals(anothercrfItem));
		anothercrfItem.setCrfItemAllignment(CrfItemAllignment.HORIZONTAL);
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


	public void testRemoveCrfItemDisplayRules() {

		crfItem.addCrfItemDisplayRules(crfItemDisplayRule1);
		crfItem.addCrfItemDisplayRules(crfItemDisplayRule2);
		crfItem.addCrfItemDisplayRules(crfItemDisplayRule3);
		crfItem.addCrfItemDisplayRules(crfItemDisplayRule4);
		crfItem.addCrfItemDisplayRules(crfItemDisplayRule5);
		assertEquals("must be 5 rules", 5, crfItem.getCrfItemDisplayRules().size());

		crfItem.removeCrfItemDisplayRulesByIds("1,3,2");

		assertEquals("must remove 3 rules", 2, crfItem.getCrfItemDisplayRules().size());

		assertTrue("must preseve the order of rules", crfItem.getCrfItemDisplayRules().contains(crfItemDisplayRule4));
		assertTrue("must preseve the order of  rules", crfItem.getCrfItemDisplayRules().contains(crfItemDisplayRule5));

	}

	public void testAddCrfItemDisplayRules() {
		assertTrue("must not have any display rules", crfItem.getCrfItemDisplayRules().isEmpty());
		CrfItemDisplayRule nullCrfItemDisplayRule = null;
		crfItem.addCrfItemDisplayRules(nullCrfItemDisplayRule);
		assertTrue("must not add null display rules", crfItem.getCrfItemDisplayRules().isEmpty());

		crfItem.addCrfItemDisplayRules(crfItemDisplayRule1);
		crfItem.addCrfItemDisplayRules(crfItemDisplayRule2);
		crfItem.addCrfItemDisplayRules(crfItemDisplayRule3);
		crfItem.addCrfItemDisplayRules(crfItemDisplayRule4);
		crfItem.addCrfItemDisplayRules(crfItemDisplayRule5);
		assertEquals("must be 5 rules", 5, crfItem.getCrfItemDisplayRules().size());

		for (CrfItemDisplayRule crfItemDisplayRule : crfItem.getCrfItemDisplayRules()) {
			assertEquals("crf item must not be null", crfItem, crfItemDisplayRule.getCrfItem());
		}


	}

	public void testMustNotAddSameCrfItemDisplayRule() {
		assertTrue("must not have any display rules", crfItem.getCrfItemDisplayRules().isEmpty());
		CrfItemDisplayRule nullCrfItemDisplayRule = null;
		crfItem.addCrfItemDisplayRules(nullCrfItemDisplayRule);
		assertTrue("must not add null display rules", crfItem.getCrfItemDisplayRules().isEmpty());

		crfItem.addCrfItemDisplayRules(crfItemDisplayRule1);
		assertEquals("must be 1 rules", 1, crfItem.getCrfItemDisplayRules().size());

		crfItem.addCrfItemDisplayRules(crfItemDisplayRule1);

		crfItem.addCrfItemDisplayRules(crfItemDisplayRule5);
		assertEquals("must be 2 rules only because same rule must not be added twice", 2, crfItem.getCrfItemDisplayRules().size());

		for (CrfItemDisplayRule crfItemDisplayRule : crfItem.getCrfItemDisplayRules()) {
			assertEquals("crf item must not be null", crfItem, crfItemDisplayRule.getCrfItem());
		}


	}

}