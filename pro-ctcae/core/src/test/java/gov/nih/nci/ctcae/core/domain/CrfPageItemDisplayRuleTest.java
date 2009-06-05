package gov.nih.nci.ctcae.core.domain;

import static gov.nih.nci.ctcae.core.helper.Fixture.createCrfPageItemDisplayRules;
import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @created Dec 17, 2008
 */
public class CrfPageItemDisplayRuleTest extends TestCase {
    private CrfPageItem crfPageItem;

    private CrfPageItemDisplayRule crfPageItemDisplayRule;
    private ProCtcValidValue proCtcValidValue1;
    private ProCtcValidValue proCtcValidValue2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        crfPageItem = new CrfPageItem();
        proCtcValidValue1 = new ProCtcValidValue();
        proCtcValidValue1.setValue("value1");
        proCtcValidValue2 = new ProCtcValidValue();
        proCtcValidValue2.setValue("value2");
        proCtcValidValue2.setId(2);
    }


    public void testGetterAndSetter() {
        crfPageItemDisplayRule = createCrfPageItemDisplayRules(1, proCtcValidValue2);

        assertEquals(Integer.valueOf(2), crfPageItemDisplayRule.getProCtcValidValue().getId());
        assertEquals(Integer.valueOf(1), crfPageItemDisplayRule.getId());

    }

    public void testEqualsAndHashCode() {

        CrfPageItemDisplayRule anotherCrfPageItemDisplayRule = null;
        assertEquals(anotherCrfPageItemDisplayRule, crfPageItemDisplayRule);

        crfPageItemDisplayRule = new CrfPageItemDisplayRule();
        assertFalse(crfPageItemDisplayRule.equals(anotherCrfPageItemDisplayRule));

        anotherCrfPageItemDisplayRule = new CrfPageItemDisplayRule();
        assertEquals(anotherCrfPageItemDisplayRule, crfPageItemDisplayRule);
        assertEquals(anotherCrfPageItemDisplayRule.hashCode(), crfPageItemDisplayRule.hashCode());

        crfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue1);
        assertFalse(crfPageItemDisplayRule.equals(anotherCrfPageItemDisplayRule));
        anotherCrfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue1);
        assertEquals(anotherCrfPageItemDisplayRule.hashCode(), crfPageItemDisplayRule.hashCode());
        assertEquals(anotherCrfPageItemDisplayRule, crfPageItemDisplayRule);

        crfPageItemDisplayRule.setCrfItem(crfPageItem);
        assertFalse(crfPageItemDisplayRule.equals(anotherCrfPageItemDisplayRule));
        anotherCrfPageItemDisplayRule.setCrfItem(crfPageItem);
        assertEquals(anotherCrfPageItemDisplayRule.hashCode(), crfPageItemDisplayRule.hashCode());
        assertEquals(anotherCrfPageItemDisplayRule, crfPageItemDisplayRule);
        proCtcValidValue1.setId(2);
        crfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue2);

        assertFalse(crfPageItemDisplayRule.equals(anotherCrfPageItemDisplayRule));
        proCtcValidValue2.setId(2);
        anotherCrfPageItemDisplayRule.setProCtcValidValue(proCtcValidValue2);
        assertEquals(anotherCrfPageItemDisplayRule, crfPageItemDisplayRule);
        assertEquals(anotherCrfPageItemDisplayRule.hashCode(), crfPageItemDisplayRule.hashCode());


    }



}