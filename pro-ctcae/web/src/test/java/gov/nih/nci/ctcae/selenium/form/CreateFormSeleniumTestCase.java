package gov.nih.nci.ctcae.selenium.form;

/**
 * @author mehul gulati
 * Date: Jan 27, 2009
 */

import com.thoughtworks.selenium.*;

import java.util.regex.Pattern;
import java.util.Date;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

public class CreateFormSeleniumTestCase extends AbstractSeleniumTestCase {
    public void testNew() throws Exception {
        selenium.open("/ctcae/pages/form/manageForm;jsessionid=49D7D0CAE625FDD18B54526844931B79");
        selenium.setSpeed("1000");
        typeAutosuggest("study-input", "p", "study-choices");
        selenium.click("//div[@id='study-choices']/ul/li[1]");
        selenium.click("newFormUrl");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());
        selenium.click("crfTitle");
    //    selenium.type("value", "testformselenium_" + new Date().getTime());
        selenium.type("crf.title", "testformselenium_" + new Date().getTime());
        selenium.click("//img[@alt='Add']");
        selenium.click("//a[@id='proCtcTerm_29']/img");
        selenium.click("//a[@id='proCtcTerm_15']/img");
        selenium.click("proCtcTerm_18");
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
    }

    }



