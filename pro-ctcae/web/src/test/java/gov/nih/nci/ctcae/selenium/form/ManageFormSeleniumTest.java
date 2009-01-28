package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author mehul gulati
 *         Date: Jan 28, 2009
 */
public class ManageFormSeleniumTest extends AbstractSeleniumTestCase {

    public void testNew() throws Exception {
        String formTitle = randomString();
        createForm(formTitle);
        selenium.open("/ctcae/pages/form/manageForm");
        selenium.setSpeed("1000");
        selenium.click("secondlevelnav_manageFormController");
        selenium.waitForPageToLoad("30000");
        typeAutosuggest("study-input", "p", "study-choices");
        selenium.click("//div[@id='study-choices']/ul/li[1]");

        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", formTitle), "10000");

        
    }
}


