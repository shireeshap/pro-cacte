package gov.nih.nci.ctcae.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.web.SeleniumProperties;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Jan 26, 2009
 */
public class AbstractSeleniumTestCase extends AbstractWebIntegrationTestCase {
    protected DefaultSelenium selenium;

    protected SeleniumProperties seleniumProperties;

    @Override
    protected String[] getConfigLocations() {
        String[] configLocations = super.getConfigLocations();
        List<String> list = new ArrayList<String>(Arrays.asList(configLocations));
        list.add("classpath*:gov/nih/nci/ctcae/selenium/applicationContext-selenium.xml");
        return list.toArray(new String[]{});


    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        selenium = createSeleniumClient(seleniumProperties.getSeleniumClientUrl());
        selenium.start();
        logger.debug("selenium server started for:" + seleniumProperties.getSeleniumClientUrl());

    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {

        deleteFromTables(new String[]{"study_participant_crf_items",
                "sp_crf_schedules",
                "study_participant_crfs",
                "crf_page_item_display_rules",
                "crf_page_items",
                "CRF_PAGES",
                "CRFS"});

        selenium.stop();

        super.onTearDownAfterTransaction();

    }

    protected DefaultSelenium createSeleniumClient(String url) throws Exception {
        DefaultSelenium defaultSelenium = new DefaultSelenium(seleniumProperties.getServerHost(), seleniumProperties.getServerPort(), seleniumProperties.getBrowser(), url);
        return defaultSelenium;
    }

    protected void createStudy() {
        String shortTitle = randomString();

        String longTitle = randomString();
        String assignedIdentifier = randomString();

        openCreateStudyPage();
        selenium.type("shortTitle", shortTitle);
        selenium.type("assignedIdentifier", assignedIdentifier);
        selenium.type("longTitle", longTitle);

        String autoComplterInputId = "studyCoordinatingCenter.organization";
        String selectedValue = "National";
        selectAutoCompleter("studyFundingSponsor.organization", "forest", "Wake");

        selectAutoCompleter(autoComplterInputId, "nci", selectedValue);


        selenium.click("submitButton");
        selenium.waitForPageToLoad("5000");


        assertTrue(selenium.isTextPresent("Confirmation"));
        assertTrue(selenium.isTextPresent(shortTitle));
        assertTrue(selenium.isTextPresent(longTitle));
        assertTrue(selenium.isTextPresent(assignedIdentifier));
        assertTrue(selenium.isTextPresent("Wake"));
        assertTrue(selenium.isTextPresent("National"));

    }

    public String randomString() {
        String name = "" + Calendar.getInstance().getTime().getTime();
        name = name.substring(name.length() - 5, name.length() - 1);
        return name;

        //      return UUID.randomUUID().toString();
    }

    public void typeStudyAutoCompleter(final String studySearchString) throws InterruptedException {
        typeAutosuggest("study-input", studySearchString, "study-choices");
    }

    protected void selectAutoCompleter(String autoComplterInputId, String input, String selectedValue) {
        String locator = autoComplterInputId + "-input";

        if (input.length() < 2) {
            fail("input must be atleast 2 char long");
        }
        selenium.type(locator, input.substring(0, 1));
        selenium.typeKeys(locator, input.substring(1, input.length()));


        String selectedText = String.format("//li[contains(text(), '%s')]", selectedValue);

        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", selectedValue), "10000");
        selenium.click(selectedText);
    }

    public void createForm(String title) throws InterruptedException {
        String formTitle = title;
        selenium.open("/ctcae/pages/form/manageForm;jsessionid=49D7D0CAE625FDD18B54526844931B79");
        selenium.setSpeed("1000");

        typeStudyAutoCompleter("p");
        selenium.click("newFormUrl");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());
        selenium.click("crfTitle");
        //    selenium.type("value", "testformselenium_" + new Date().getTime());
        selenium.type("crf.title", formTitle);
        selenium.click("//img[@alt='Add']");
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("The Form was saved successfully"));

    }

    public void searchForm(String title) throws InterruptedException {
        String formTitle = title;
        selenium.open("/ctcae/pages/form/manageForm");
        selenium.setSpeed("1000");
        selenium.click("secondlevelnav_manageFormController");
        selenium.waitForPageToLoad("30000");
        typeAutosuggest("study-input", "p", "study-choices");
        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", formTitle), "10000");

    }


    public void typeAutosuggest(String element, String text, String elemPresent)
            throws InterruptedException {
        selenium.click(element);
        selenium.typeKeys(element, "");
        selenium.typeKeys(element, text);
        waitForElementPresent("//div[@id='" + elemPresent + "']/ul/li");

        selenium.click("//div[@id='" + elemPresent + "']/ul/li[1]");
        selenium.click(element);
    }

    public void waitForElementPresent(String xPathElement)
            throws InterruptedException {
        for (int second = 0; ; second++) {
            if (second >= 60)
                fail("Timed out waiting for element to be created: "
                        + xPathElement);
            try {
                if (selenium.isElementPresent(xPathElement))
                    break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);

        }
    }


    public void loginAdmin() {
        login("SYSTEM_ADMIN", "system_admin");
    }

    public void login(String userId, String password) {
        selenium.type("j_username", userId);
        selenium.type("j_password", password);

        selenium.submit("login");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());
    }
//
//    public void testSystemAdminLogin() {
//
////        openLoginPage();
////        loginAdmin();
////        assertEquals("caAERS || Welcome to caAERS", selenium.getTitle());
//
//    }

    protected void openLoginPage() {
        selenium.open("/public/login");
    }

    protected void openCreateStudyPage() {
        openPage("/study/createStudy");
    }

    protected void openSearchStudyPage() {
        openPage("/study/searchStudy");
    }

    private void openPage(String pageUrl) {
        selenium.open(seleniumProperties.getBaseUrl() + pageUrl);
        if (selenium.isTextPresent("Enter caAERS")) {
            loginAdmin();
        }
    }


    @Required
    public void setSeleniumProperties(SeleniumProperties seleniumProperties) {
        this.seleniumProperties = seleniumProperties;
    }
}
