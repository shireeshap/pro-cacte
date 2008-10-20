package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public class StudyAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private StudyAjaxFacade studyAjaxFacade;
    protected Map parameterMap;
    private String type;
    private String text;
    private StudyRepository studyRepository;
    private Study study;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        study = Fixture.createStudy("study short title", "study long title", "assigned identifier");

        study = studyRepository.save(study);


    }


    public void testSearchStudiesByShortTitle() {
        type = "shortTitle";
        text = "short";

        String table = studyAjaxFacade.searchStudies(parameterMap, type, text, request);
        assertNotNull(table);
        assertTrue("must find atleast study matching with short title", table.contains(study.getShortTitle()));

    }


    public void testSearchStudiesByIdentifier() {
        type = "assignedIdentifier";
        text = "identifier";

        String table = studyAjaxFacade.searchStudies(parameterMap, type, text, request);
        assertNotNull(table);
        assertTrue("must find atleast study matching with identifier", table.contains(study.getAssignedIdentifier()));

    }

    public void setStudyAjaxFacade(StudyAjaxFacade studyAjaxFacade) {
        this.studyAjaxFacade = studyAjaxFacade;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
