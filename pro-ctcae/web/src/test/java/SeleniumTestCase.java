import com.thoughtworks.selenium.DefaultSelenium;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.Calendar;
import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Oct 22, 2008
 */
public class SeleniumTestCase extends AbstractWebIntegrationTestCase {

    private StudyRepository studyRepository;
    DefaultSelenium selenium;
    String waitTime = "50000";

    protected String NCI_CODE;


    protected DefaultSelenium createSeleniumClient(String url) throws Exception {
        DefaultSelenium defaultSelenium = new DefaultSelenium("localhost", 4444, "*chrome", url);
        return defaultSelenium;
    }

    public void testSystemAdminLogin() {

        selenium.open("/public/login");
        loginAdmin();
        assertEquals("caAERS || Welcome to caAERS", selenium.getTitle());

    }

    public void testCreateStudy() {
        Study study = new Study();
        study.setShortTitle("shortTitle");
        study.setLongTitle("longTitle");
        study.setAssignedIdentifier("longTitle");

        createStudy(study);
        validateStudyCreation(study);


    }

    public void testSearchStudyByShorTitle() {

        searchStudy("%");

        StudyQuery query = new StudyQuery();
        query.filterStudiesByShortTitle("%");

        Collection<Study> studies = studyRepository.find(query);

        assertTrue("must found correct no of results;" + studies.size(), selenium.isTextPresent(studies.size() + ""));
        selenium.select("//select[@name='ajaxTable_rd']", "100");

        String text = "";
        if (studies.size() < 100) {
            text = studies.size() + " results found, displaying 1 to " + studies.size();
        } else {
            text = studies.size() + " results found, displaying 1 to 100";

        }

        selenium.waitForCondition(String.format("selenium.isTextPresent('%s')", text), "10000");
        assertTrue("must found correct no of results;" + studies.size(), selenium.isTextPresent(text));


    }

    private void searchStudy(String searchText) {
        openPage("/study/searchStudy");
        selenium.type("searchText", searchText);
        selenium.click("searchButton");
        selenium.waitForCondition(String.format("selenium.isTextPresent('results found')"), "10000");


    }

    public void testCreateOrganiationForExistingIdentifier() {

        //createStudy("Org name", NCI_CODE);
        //validateStudyCreation("Org name", NCI_CODE);

        //now create the same organization again

        //createStudy("another org name", NCI_CODE);

        assertTrue("two organizations must not have same identifier", selenium.isTextPresent("Nci Identifier already exits in the datbase"));

    }

    protected void createStudy(Study study) {
        openPage("/study/createStudy");
        selenium.type("shortTitle", study.getShortTitle());
        selenium.type("assignedIdentifier", study.getAssignedIdentifier());
        selenium.type("longTitle", study.getLongTitle());

        String autoComplterInputId = "studyCoordinatingCenter.organization";
        String selectedValue = "National";
        selectAutoCompleter("studyFundingSponsor.organization", "forest", "Wake");

        selectAutoCompleter(autoComplterInputId, "nci", selectedValue);


        selenium.click("submitButton");
        selenium.waitForPageToLoad("5000");

        assertTrue(true);

    }

    private void selectAutoCompleter(String autoComplterInputId, String input, String selectedValue) {
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

    protected void openPage(String pageUrl) {
        selenium.open("http://localhost:8040/ctcae/pages" + pageUrl);
        if (selenium.isTextPresent("Enter caAERS")) {
            loginAdmin();
        }
    }

    protected void validateStudyCreation(Study study) {

        assertTrue(selenium.isTextPresent("Confirmation"));
        assertTrue(selenium.isTextPresent(study.getShortTitle()));
        assertTrue(selenium.isTextPresent(study.getLongTitle()));
        // assertTrue(selenium.isTextPresent(study.getAssignedIdentifier()));
        assertTrue(selenium.isTextPresent("Wake"));
        assertTrue(selenium.isTextPresent("National"));


    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        NCI_CODE = "NCI" + getRandonDigit();
        selenium = createSeleniumClient("http://localhost:8040/ctcae");
        selenium.start();


    }


    protected String getRandonDigit() {
        String name = "" + Calendar.getInstance().getTime().getTime();
        name = name.substring(name.length() - 5, name.length() - 1);
        return name;
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();

        // selenium.stop();

    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void loginAdmin() {
        login("SYSTEM_ADMIN", "system_admin");
    }

    public void login(String userId, String password) {
        selenium.type("j_username", userId);
        selenium.type("j_password", password);

        selenium.submit("login");
        selenium.waitForPageToLoad(waitTime);
    }
}
