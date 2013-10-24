package gov.nih.nci.ctcae.selenium.form;

/**
 * @author mehul gulati
 * Date: Jan 27, 2009
 */

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

import java.util.Date;

public class CreateFormSeleniumTest extends AbstractSeleniumTestCase {

    public void testNew() throws Exception {
        selenium.open("/proctcae/pages/form/manageForm");
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

    public void testCreateNewFormByAddingSymptoms() throws Exception {

        selenium.open("/proctcae/pages/form/manageForm");
        selenium.setSpeed("1000");
        typeAutosuggest("study-input", "p", "study-choices");
        selenium.click("newFormUrl");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());

        selenium.type("crf.title", "testformselenium_" + new Date().getTime());
        selenium.click("//img[@alt='Add']");
        selenium.click("//a[@id='proCtcTerm_-2']/img");
        //	selenium.click("crf.crfPagesSortedByPageNumber[0].description-property");
        selenium.type("crf.crfPagesSortedByPageNumber[0].description", "page 1");
        //	selenium.click("crf.crfPagesSortedByPageNumber[1].description-property");
        selenium.type("crf.crfPagesSortedByPageNumber[1].description", "page 2");

        postProcessFormSave();

        assertTrue(selenium.isTextPresent("page 1"));
        assertTrue(selenium.isTextPresent("page 2"));


    }

    public void testCreateNewFormByAddingCategory() throws Exception {

        selenium.open("/proctcae/pages/form/manageForm");
        selenium.setSpeed("1000");
        typeAutosuggest("study-input", "p", "study-choices");
        selenium.click("newFormUrl");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());

        selenium.type("crf.title", "testformselenium_" + new Date().getTime());
        selenium.click("//img[@alt='Add']");
        selenium.click("//a[@id='ctcCategory_307']/img");

        postProcessFormSave();

        assertTrue(selenium.isTextPresent(FATIGUE_SEVERE_QUESTION_TEXT));
        assertTrue(selenium.isTextPresent(FATIGUE_FREQUENCY_QUESTION_TEXT));
        assertTrue(selenium.isTextPresent(FATIGUE_INTERFERENCE_QUESTION_TEXT));


    }
}





