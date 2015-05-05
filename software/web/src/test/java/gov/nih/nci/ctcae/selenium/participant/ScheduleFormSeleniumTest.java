package gov.nih.nci.ctcae.selenium.participant;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Feb 2, 2009
 */
public class ScheduleFormSeleniumTest extends AbstractSeleniumTestCase {
    	public void testNew() throws Exception {
            String title = randomString();
            createForm(title);
            searchForm(title);
            selenium.setSpeed("1000");
            selenium.click("link=Release");
            selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", "Release form"), "10000");
            selenium.click("flow-update");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isTextPresent("Released"));

        selenium.open("/proctcae/pages/participant/schedulecrf");
		selenium.waitForPageToLoad("30000");
		selenium.click("secondlevelnav_scheduleCrfController");
		selenium.waitForPageToLoad("30000");
            typeAutosuggest("participant-input", "j", "participant-choices");
            typeAutosuggest("study-input", "p", "study-choices");
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
		selenium.type("repetitionPeriodAmount_0", "3");
		selenium.type("repeatUntilValue_0", "4");
		selenium.click("//input[@value='Apply']");
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("The schedules were saved successfully"));
		assertTrue(selenium.isTextPresent(title));
		assertTrue(selenium.isTextPresent("Scheduled"));
	}
}
