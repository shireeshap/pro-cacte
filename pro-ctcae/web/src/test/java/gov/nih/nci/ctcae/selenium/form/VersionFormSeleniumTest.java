package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 *         Date: Jan 29, 2009
 */
public class VersionFormSeleniumTest extends AbstractSeleniumTestCase {

    public void testVersionForm() throws Exception {
        String formTitle = randomString();
        createForm(formTitle);
        searchForm(formTitle);
        selenium.setSpeed("1000");
        selenium.click("link=Release");
        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", "Release form"), "10000");
        selenium.click("flow-update");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Version");
        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", "Version Form"), "10000");
        selenium.click("flow-update");
        selenium.waitForPageToLoad("30000");
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
        selenium.click("secondlevelnav_manageFormController");
        selenium.waitForPageToLoad("30000");
        searchForm(formTitle);
        assertTrue(selenium.isTextPresent("2.0"));
    }
}
    
