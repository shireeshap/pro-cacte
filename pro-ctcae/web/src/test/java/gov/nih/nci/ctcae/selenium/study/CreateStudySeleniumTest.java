package gov.nih.nci.ctcae.selenium.study;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Feb 1, 2009
 */
public class CreateStudySeleniumTest  extends AbstractSeleniumTestCase {
    	public void testNew() throws Exception {

            String shortTitle = randomString();
            String longTitle = randomString();
            String assignedIdentifier = randomString();

        selenium.open("ctcae/pages/study/createStudy");
        selenium.setSpeed("1000");
        selenium.click("firstlevelnav_createStudyController");
		selenium.waitForPageToLoad("30000");
        selenium.type("study.assignedIdentifier", assignedIdentifier);
		selenium.type("study.shortTitle", shortTitle);
		selenium.type("study.longTitle", longTitle);
        typeAutosuggest("study.studyCoordinatingCenter.organization-input", "h", "study.studyCoordinatingCenter.organization-choices");
 		typeAutosuggest("study.studyFundingSponsor.organization-input", "c", "study.studyFundingSponsor.organization-choices");
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@value='Add Study Site']");
        typeAutosuggest("study.studySites[0].organization-input", "n", "study.studySites[0].organization-choices");
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Overview"));
		assertTrue(selenium.isTextPresent(shortTitle));
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("The Study was saved successfully"));
		assertTrue(selenium.isTextPresent(shortTitle));
	}
    
}
