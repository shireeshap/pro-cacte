package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Jan 30, 2009
 */
public class UniqueFormTitleSeleniumTest extends AbstractSeleniumTestCase {

    public void testNew() throws Exception {
        String formTitle = randomString();
        createForm(formTitle);
        selenium.setSpeed("1000");
        selenium.open("/ctcae/pages/form/basicForm?studyId=-1001");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());
        selenium.click("crfTitle");
        selenium.type("crf.title", formTitle);
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Title already exists in database"));

    }
}
