package gov.nih.nci.ctcae.selenium.study;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 *         Date: Feb 1, 2009
 */
public class SearchStudySeleniumTest extends AbstractSeleniumTestCase {
    public void testNew() throws Exception {
        String study = randomString();
        createStudyOnUI(study);
        selenium.click("secondlevelnav_searchStudyController");
        selenium.waitForPageToLoad("30000");
        selenium.type("searchText", study);
        selenium.click("searchButton");
        assertTrue(selenium.isTextPresent("Results"));
        assertTrue(selenium.isTextPresent("1 results found"));
    }
}
