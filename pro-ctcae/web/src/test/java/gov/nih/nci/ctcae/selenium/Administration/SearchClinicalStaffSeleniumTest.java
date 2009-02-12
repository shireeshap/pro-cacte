package gov.nih.nci.ctcae.selenium.Administration;

import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

/**
 * @author Mehul Gulati
 * Date: Feb 2, 2009
 */
public class SearchClinicalStaffSeleniumTest extends AbstractSeleniumTestCase {
    public void testNew() throws Exception {
            String firstName = randomString();
            String lastName = randomString();
        selenium.setSpeed("1000");
		selenium.open("/ctcae/pages/form/manageForm");
		selenium.click("firstlevelnav_createClinicalStaffController");
		selenium.waitForPageToLoad("30000");
		selenium.click("secondlevelnav_createClinicalStaffController");
        selenium.waitForPageToLoad("30000");
		selenium.type("clinicalStaff.firstName", firstName);
		selenium.type("clinicalStaff.lastName", lastName);
		selenium.type("clinicalStaff.nciIdentifier", "11");
		selenium.type("clinicalStaff.emailAddress", "johndoe@sb.com");
		selenium.type("clinicalStaff.phoneNumber", "123 123 1234");
		selenium.click("//input[@value='Add Site']");
            typeAutosuggest("clinicalStaff.clinicalStaffAssignments[0].organization-input", "n", "clinicalStaff.clinicalStaffAssignments[0].organization-choices");
		selenium.click("flow-next");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("The Clinical Staff was saved successfully"));

        selenium.open("/ctcae/pages/admin/clinicalStaff/createClinicalStaff");
        selenium.click("link=Search Clinical Staff");
        selenium.waitForPageToLoad("30000");
        selenium.type("firstName", firstName);
        selenium.click("//input[@value='Search']");
        assertTrue(selenium.isTextPresent("Results"));
        assertTrue(selenium.isTextPresent("1 results found"));
        assertTrue(selenium.isTextPresent("11"));
    }


}
