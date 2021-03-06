package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import junit.framework.TestCase;

import static gov.nih.nci.ctcae.core.helper.Fixture.createCrfPageItemDisplayRules;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class CrfItemTest extends TestCase {
    private CrfPageItem crfPageItem;

    private CrfPageItemDisplayRule crfPageItemDisplayRule1, crfPageItemDisplayRule2, crfPageItemDisplayRule3, crfPageItemDisplayRule4, crfPageItemDisplayRule5;
    ProCtcValidValue proCtcValidValue1, proCtcValidValue2, proCtcValidValue3, proCtcValidValue4, proCtcValidValue5;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        crfPageItem = new CrfPageItem();

        proCtcValidValue1 = new ProCtcValidValue();
        proCtcValidValue1.setValue("value1", SupportedLanguageEnum.ENGLISH);
        proCtcValidValue1.setId(-1);

        proCtcValidValue2 = new ProCtcValidValue();
        proCtcValidValue2.setValue("value2", SupportedLanguageEnum.ENGLISH);
        proCtcValidValue2.setId(-2);

        proCtcValidValue3 = new ProCtcValidValue();
        proCtcValidValue3.setValue("value3", SupportedLanguageEnum.ENGLISH);
        proCtcValidValue3.setId(-3);

        proCtcValidValue3 = new ProCtcValidValue();
        proCtcValidValue3.setValue("value4", SupportedLanguageEnum.ENGLISH);
        proCtcValidValue3.setId(-4);

        proCtcValidValue4 = new ProCtcValidValue();
        proCtcValidValue4.setValue("value5", SupportedLanguageEnum.ENGLISH);
        proCtcValidValue4.setId(-5);

        proCtcValidValue5 = new ProCtcValidValue();
        proCtcValidValue5.setValue("value6", SupportedLanguageEnum.ENGLISH);
        proCtcValidValue5.setId(-6);


        crfPageItemDisplayRule1 = createCrfPageItemDisplayRules(1, proCtcValidValue1);
        crfPageItemDisplayRule2 = createCrfPageItemDisplayRules(2, proCtcValidValue2);
        crfPageItemDisplayRule3 = createCrfPageItemDisplayRules(3, proCtcValidValue3);
        crfPageItemDisplayRule4 = createCrfPageItemDisplayRules(4, proCtcValidValue4);
        crfPageItemDisplayRule5 = createCrfPageItemDisplayRules(5, proCtcValidValue5);


    }


    public void testConstructor() {
        assertEquals(Integer.valueOf(0), crfPageItem.getDisplayOrder());
        assertTrue("must require response", crfPageItem.getResponseRequired());
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
        anothercrfPageItem.setResponseRequired(Boolean.FALSE);
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





    public void testAddCrfPageItemDisplayRules() {
        assertTrue("must not have any display rules", crfPageItem.getCrfPageItemDisplayRules().isEmpty());
        CrfPageItemDisplayRule nullCrfPageItemDisplayRule = null;
        crfPageItem.addCrfPageItemDisplayRules(nullCrfPageItemDisplayRule);
        assertTrue("must not add null display rules", crfPageItem.getCrfPageItemDisplayRules().isEmpty());

        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule1);
        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule2);
        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule3);
        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule4);
        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule5);
        assertEquals("must be 5 rules", 5, crfPageItem.getCrfPageItemDisplayRules().size());

        for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
            assertEquals("crf item must not be null", crfPageItem, crfPageItemDisplayRule.getCrfItem());
        }


    }

    public void testMustNotAddSameCrfPageItemDisplayRule() {
        assertTrue("must not have any display rules", crfPageItem.getCrfPageItemDisplayRules().isEmpty());
        CrfPageItemDisplayRule nullCrfPageItemDisplayRule = null;
        crfPageItem.addCrfPageItemDisplayRules(nullCrfPageItemDisplayRule);
        assertTrue("must not add null display rules", crfPageItem.getCrfPageItemDisplayRules().isEmpty());

        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule1);
        assertEquals("must be 1 rules", 1, crfPageItem.getCrfPageItemDisplayRules().size());

        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule1);

        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule5);
        assertEquals("must be 2 rules only because same rule must not be added twice", 2, crfPageItem.getCrfPageItemDisplayRules().size());

        for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
            assertEquals("crf item must not be null", crfPageItem, crfPageItemDisplayRule.getCrfItem());
        }


    }

}