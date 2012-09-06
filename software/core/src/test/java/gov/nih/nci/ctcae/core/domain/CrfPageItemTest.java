package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Vinay Kumar
 * @created Dec 17, 2008
 */
public class CrfPageItemTest extends TestCase {
    private CrfPageItem crfPageItem;

    private CrfPageItemDisplayRule crfPageItemDisplayRule1, crfPageItemDisplayRule2;
    private ProCtcValidValue proCtcValidValue1;
    private ProCtcValidValue proCtcValidValue2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        crfPageItem = new CrfPageItem();
        proCtcValidValue1 = new ProCtcValidValue();
        proCtcValidValue1.setValue("value1", SupportedLanguageEnum.ENGLISH);
        proCtcValidValue1.setId(1);

        proCtcValidValue2 = new ProCtcValidValue();
        proCtcValidValue2.setValue("value2", SupportedLanguageEnum.ENGLISH);
        proCtcValidValue2.setId(2);

        crfPageItemDisplayRule1 = new CrfPageItemDisplayRule();
        crfPageItemDisplayRule1.setProCtcValidValue(proCtcValidValue1);

        crfPageItemDisplayRule2 = new CrfPageItemDisplayRule();
        crfPageItemDisplayRule2.setProCtcValidValue(proCtcValidValue2);

        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule1);
        crfPageItem.addCrfPageItemDisplayRules(crfPageItemDisplayRule2);


    }


    public void testRemoveCrfItemDisplayRules() {
        assertEquals(2, crfPageItem.getCrfPageItemDisplayRules().size());

        Set<Integer> proCtcValidValues = new HashSet<Integer>();

        proCtcValidValues.add(proCtcValidValue1.getId());

        crfPageItem.removeCrfPageItemDisplayRulesByIds(proCtcValidValues);
        assertEquals("must remove 1 rule", 1, crfPageItem.getCrfPageItemDisplayRules().size());

    }


}