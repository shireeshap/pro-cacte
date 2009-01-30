package gov.nih.nci.ctcae.selenium.form;

/**
 * @author mehul gulati
 * Date: Jan 27, 2009
 */

import com.thoughtworks.selenium.*;

import java.util.regex.Pattern;
import java.util.Date;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

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
		selenium.click("//a[@id='proCtcTerm_-2']/img");
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("The Form was saved successfully"));
        selenium.click("secondlevelnav_manageFormController");
        selenium.waitForPageToLoad("30000");
        typeAutosuggest("study-input", "p", "study-choices");
        assertFalse(selenium.isTextPresent("Copy | Version"));


    }

}





