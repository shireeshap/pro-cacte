package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Jan 29, 2009
 */
public class CopyFormSeleniumTest extends AbstractSeleniumTestCase {
    	public void testNew() throws Exception {
            String formTitle = randomString();
            createForm(formTitle);
            searchForm(formTitle);
		selenium.click("link=Copy");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("You have successfully created a copy of this form"));
        selenium.click("//img[@alt='Delete']");
        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", "Delete"), "10000");
        selenium.click("//a[@id='proCtcTerm_-2']/img");    
        selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("The Form was saved successfully"));
	}

}
