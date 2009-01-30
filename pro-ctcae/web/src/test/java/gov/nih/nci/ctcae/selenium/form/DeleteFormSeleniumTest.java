package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 *         Date: Jan 29, 2009
 */
public class DeleteFormSeleniumTest extends AbstractSeleniumTestCase {

    public void testDelete() throws Exception {
        String formTitle = randomString();
        createForm(formTitle);
        searchForm(formTitle);
        selenium.setSpeed("1000");
        selenium.click("link=Delete");
        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", "Delete Form"), seleniumProperties.getWaitTime());
        selenium.click("flow-update");
        selenium.waitForPageToLoad("30000");
        selenium.click("secondlevelnav_manageFormController");
        selenium.waitForPageToLoad("30000");
        typeAutosuggest("study-input", "p", "study-choices");
        assertFalse(selenium.isTextPresent(formTitle));
    }
}
