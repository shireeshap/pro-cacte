package gov.nih.nci.ctcae.selenium.participant;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Feb 2, 2009
 */
public class SearchParticipantSeleniumTest extends AbstractSeleniumTestCase {
	public void testNew() throws Exception {
        String participant = randomString();
        createParticipant(participant);
        selenium.open("/proctcae/pages/participant/create");
		selenium.click("secondlevelnav_searchParticipantController");
		selenium.waitForPageToLoad("30000");
		selenium.type("firstName", participant);
		selenium.click("//input[@value='Search']");
		assertTrue(selenium.isTextPresent("Results"));
		assertTrue(selenium.isTextPresent("1 results found"));
	}    
}
