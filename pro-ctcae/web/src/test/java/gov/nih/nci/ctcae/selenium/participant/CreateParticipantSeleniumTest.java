package gov.nih.nci.ctcae.selenium.participant;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Feb 2, 2009
 */
public class CreateParticipantSeleniumTest extends AbstractSeleniumTestCase {
    	public void testNew() throws Exception {
            String firstName = randomString();
            String lastName = randomString();
        selenium.open("/ctcae/pages/participant/create");
        selenium.setSpeed("1000");
        selenium.click("secondlevelnav_createParticipantController");
		selenium.waitForPageToLoad("30000");
		selenium.select("siteId", "label=National Cancer Institute ( NCI )");
		selenium.type("participant.firstName", firstName);
		selenium.type("participant.lastName", lastName);
		selenium.type("participant.assignedIdentifier", "NCI 1");
		selenium.click("participant.birthDate");
		selenium.type("participant.birthDate", "12/12/1981");
		selenium.select("participant.gender", "label=Male");
		selenium.click("studyId");
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Participant was saved successfully"));
		assertTrue(selenium.isTextPresent(firstName));
	}
}
