package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 *         Date: Jan 29, 2009
 */
public class EditFormSeleniumTest extends AbstractSeleniumTestCase {

    public void testEditForm() throws Exception {
        String formTitle = randomString();
        createForm(formTitle);

        selenium.open("/ctcae/pages/form/manageForm");
        selenium.setSpeed("1000");
        typeStudyAutoCompleter("p");
        selenium.click("link=Edit");


        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());

        updateCrfTitle("abc");


        postProcessFormSave();
        assertTrue(selenium.isTextPresent("abc"));


    }


}
