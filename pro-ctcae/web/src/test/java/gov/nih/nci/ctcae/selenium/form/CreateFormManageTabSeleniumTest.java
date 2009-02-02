package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Jan 30, 2009
 */
public class CreateFormManageTabSeleniumTest extends AbstractSeleniumTestCase {
     public void testNew() throws Exception {
     String formTitle = randomString();
        selenium.open("/ctcae/pages/form/manageForm");
        selenium.setSpeed("1000");
        typeAutosuggest("study-input", "p", "study-choices");
        selenium.click("newFormUrl");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());
        selenium.click("crfTitle");
        //    selenium.type("value", "testformselenium_" + new Date().getTime());
        selenium.type("crf.title", formTitle);
        selenium.click("//img[@alt='Add']");
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("The Form was saved successfully"));
     }
}
