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
    protected final String FATIGUE_FREQUENCY_QUESTION_TEXT = "how OFTEN did you have Fatigue ";
    protected final String FATIGUE_SEVERE_QUESTION_TEXT = "what was the WORST SEVERITY of your Fatigue";
    protected final String FATIGUE_INTERFERENCE_QUESTION_TEXT = " how much did Fatigue INTERFERE";

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

        dropData();

    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {

        dropData();

        selenium.stop();

        super.onTearDownAfterTransaction();

    }

    private void dropData() {
        deleteFromTables(new String[]{"study_participant_crf_items",
                "sp_crf_schedules",
                "study_participant_crfs",
                "crf_page_item_display_rules",
                "crf_page_items",
                "CRF_PAGES",
                "CRFS"});
    }

    protected DefaultSelenium createSeleniumClient(String url) throws Exception {
        DefaultSelenium defaultSelenium = new DefaultSelenium(seleniumProperties.getServerHost(), seleniumProperties.getServerPort(), seleniumProperties.getBrowser(), url);
        return defaultSelenium;
    }

    protected void createStudyOnUI(String study) throws InterruptedException {
        String shortTitle = study;
        String longTitle = study;
        String assignedIdentifier = study;

        selenium.open("ctcae/pages/study/createStudy");
        selenium.setSpeed("1000");
        selenium.click("firstlevelnav_studyController");
        selenium.waitForPageToLoad("30000");
        selenium.type("study.assignedIdentifier", assignedIdentifier);
        selenium.type("study.shortTitle", shortTitle);
        selenium.type("study.longTitle", longTitle);
        typeAutosuggest("study.dataCoordinatingCenter.organization-input", "h", "study.dataCoordinatingCenter.organization-choices");
        typeAutosuggest("study.studySponsor.organization-input", "c", "study.studySponsor.organization-choices");
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
        selenium.click("//input[@value='Add Study Site']");
        typeAutosuggest("study.studySites[0].organization-input", "n", "study.studySites[0].organization-choices");
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Overview"));
        assertTrue(selenium.isTextPresent(shortTitle));
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("The Study was saved successfully"));
        assertTrue(selenium.isTextPresent(shortTitle));
    }

    protected void createParticipant(String participant) throws InterruptedException {
        String firstName = participant;
        String lastName = participant;
        selenium.open("/proctcae/pages/participant/create");
        selenium.setSpeed("1000");
        selenium.click("secondlevelnav_createParticipantController");
        selenium.waitForPageToLoad("30000");
        selenium.select("siteId", "label=National Cancer Institute ( NCI )");
        selenium.type("participant.firstName", firstName);
        selenium.type("participant.lastName", lastName);
        selenium.type("participant.assignedIdentifier", "NCI 1");
        selenium.click("participant.birthDate");
        selenium.type("participant.birthDate", "12/12/1981");
        selenium.select("participant.gender", "label=Male");
        selenium.click("studyId");
        selenium.click("flow-next");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Participant was saved successfully"));
        assertTrue(selenium.isTextPresent(firstName));

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
        openManageForm();
        selenium.setSpeed("1000");

        typeStudyAutoCompleter("p");
        selenium.click("newFormUrl");

        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());


        updateCrfTitle(formTitle);

        addFatigueTerm();

        postProcessFormSave();


    }

    public void openManageForm() {
        selenium.open("/proctcae/pages/form/manageForm");
    }

    public void updateCrfTitle(String formTitle) {
        selenium.click("crfTitle");
        selenium.type("crf.title", formTitle);
    }

    public void addConstipationTerm() {
        selenium.click("//a[@id='proCtcTerm_-2']/img");
        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", "how OFTEN did you have Constipation"), seleniumProperties.getWaitTime());

    }

    public void addFatigueTerm() {
        selenium.click("//img[@alt='Add']");
        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", "how OFTEN did you have Fatigue "), seleniumProperties.getWaitTime());

    }

    public void postProcessFormSave() {
        addConstipationTerm();

        selenium.click("flow-next");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());
        assertTrue(selenium.isTextPresent("The Form was saved successfully"));

    }

    public void searchForm(String title) throws InterruptedException {
        String formTitle = title;
        openManageForm();
        selenium.setSpeed("1000");
        selenium.click("secondlevelnav_manageFormController");
        selenium.waitForPageToLoad(seleniumProperties.getWaitTime());
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
        login("system_admin", "system_admin");
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
