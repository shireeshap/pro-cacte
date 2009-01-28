package gov.nih.nci.ctcae.selenium.form;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Jan 28, 2009
 */
public class ReleaseFormSeleniumTest extends AbstractSeleniumTestCase {

        public void testNew() throws Exception {
            String formTitle = randomString();
            createForm(formTitle);
            searchForm(formTitle);
            selenium.setSpeed("1000");
            selenium.click("link=Release");
            assertTrue(selenium.isTextPresent("Release form"));            
            selenium.click("flow-update");
            selenium.waitForPageToLoad("30000");
            assertTrue(selenium.isTextPresent("Released"));
        }
    }


