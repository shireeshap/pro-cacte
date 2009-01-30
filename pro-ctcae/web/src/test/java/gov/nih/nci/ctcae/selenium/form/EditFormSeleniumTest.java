package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Jan 29, 2009
 */
public class EditFormSeleniumTest extends AbstractSeleniumTestCase {
    	public void testNew() throws Exception {
	        String formTitle = randomString();
            createForm(formTitle);

        selenium.open("/ctcae/pages/form/manageForm");
        selenium.setSpeed("1000");
        typeAutosuggest("study-input", "p", "study-choices");
		selenium.click("link=Edit");
		selenium.waitForPageToLoad("30000");
		selenium.click("crfTitle");
		selenium.type("value", "abc");
		selenium.click("//a[@id='proCtcTerm_-2']/img");
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("The Form was saved successfully"));
        assertTrue(selenium.isTextPresent("abc"));


    }
}
