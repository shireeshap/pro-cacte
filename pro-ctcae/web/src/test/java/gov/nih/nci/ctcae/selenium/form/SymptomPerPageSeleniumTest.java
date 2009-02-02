package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

import java.util.Date;

/**
 * @author Mehul Gulati
 * Date: Feb 1, 2009
 */
public class SymptomPerPageSeleniumTest extends AbstractSeleniumTestCase {

    public void testNew() throws Exception {

        selenium.open("/ctcae/pages/form/manageForm");
        selenium.setSpeed("1000");
        typeAutosuggest("study-input", "p", "study-choices");
        selenium.click("newFormUrl");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());
  //      selenium.click("crfTitle");
        //    selenium.type("value", "testformselenium_" + new Date().getTime());
        selenium.type("crf.title", "testformselenium_" + new Date().getTime());
   		selenium.click("//img[@alt='Add']");
		selenium.click("//a[@id='proCtcTerm_-2']/img");
	//	selenium.click("crf.crfPagesSortedByPageNumber[0].description-property");
		selenium.type("crf.crfPagesSortedByPageNumber[0].description", "page 1");
	//	selenium.click("crf.crfPagesSortedByPageNumber[1].description-property");
		selenium.type("crf.crfPagesSortedByPageNumber[1].description", "page 2");
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("The Form was saved successfully"));
        assertTrue(selenium.isTextPresent("page 1"));
        assertTrue(selenium.isTextPresent("page 2"));



    }

}
