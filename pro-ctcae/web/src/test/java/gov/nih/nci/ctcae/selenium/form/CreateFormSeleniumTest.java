package gov.nih.nci.ctcae.selenium.form;

/**
 * @author mehul gulati
 * Date: Jan 27, 2009
 */

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

import java.util.Date;

public class CreateFormSeleniumTest extends AbstractSeleniumTestCase {
    public void testNew() throws Exception {
        selenium.open("/ctcae/pages/form/manageForm");
        selenium.setSpeed("1000");
        typeAutosuggest("study-input", "p", "study-choices");
        selenium.click("newFormUrl");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());
        selenium.click("crfTitle");
        //    selenium.type("value", "testformselenium_" + new Date().getTime());
        selenium.type("crf.title", "testformselenium_" + new Date().getTime());
        selenium.click("//img[@alt='Add']");
        selenium.click("//img[@alt='Add']");

        postProcessFormSave();

        selenium.click("secondlevelnav_manageFormController");
        selenium.waitForPageToLoad("30000");
        typeAutosuggest("study-input", "p", "study-choices");
        assertFalse(selenium.isTextPresent("Copy | Version"));


    }

}





