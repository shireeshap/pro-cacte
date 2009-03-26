package gov.nih.nci.ctcae.core.rules;

import com.semanticbits.rules.utils.RuleUtil;
import junit.framework.TestCase;


public class RuleUtilTest extends TestCase {

    public void testGetPackageName() {

        String packagePrefix = "gov.nih.nci.cabig.caaers.rules.institution";
        String domainObjectValue = "National Cancer Institute";
        String ruleSetName = "Test RuleSet";

        String packageName = RuleUtil.getPackageName(packagePrefix, domainObjectValue, ruleSetName);

        assertEquals("gov.nih.nci.cabig.caaers.rules.institution.national_cancer_institute.test_ruleset", packageName);

    }
}