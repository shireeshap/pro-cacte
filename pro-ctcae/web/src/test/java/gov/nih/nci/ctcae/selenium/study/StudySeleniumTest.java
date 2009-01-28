package gov.nih.nci.ctcae.selenium.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.selenium.AbstractSeleniumTestCase;

import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Oct 22, 2008
 */
public class StudySeleniumTest extends AbstractSeleniumTestCase {

    private StudyRepository studyRepository;


    public void testCreateStudy() {
        createStudy();


    }

    public void testSearchStudyByShortTitle() {

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
        openSearchStudyPage();
        selenium.type("searchText", searchText);
        selenium.click("searchButton");
        selenium.waitForCondition(String.format("selenium.isTextPresent('results found')"), "10000");


    }


//    public void testCreateOrganiationForExistingIdentifier() {
//
//        //createStudy("Org name", NCI_CODE);
//        //validateStudyCreation("Org name", NCI_CODE);
//
//        //now create the same organization again
//
//        //createStudy("another org name", NCI_CODE);
//
//      //  assertTrue("two organizations must not have same identifier", selenium.isTextPresent("Nci Identifier already exists in the datbase"));
//
//    }
//

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }


}
