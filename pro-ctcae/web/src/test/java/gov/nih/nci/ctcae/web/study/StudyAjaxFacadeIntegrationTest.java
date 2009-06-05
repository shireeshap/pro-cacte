package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.Collection;
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
    private Study study;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        study = Fixture.createStudy("study short title", "study long title", "assigned identifier");

        study = studyRepository.save(study);


    }

    public void testFindByMatchingText() {


        Collection<? extends Study> studies = studyAjaxFacade.matchStudy("s");
        assertFalse(studies.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from studies studies where lower (studies.long_title) like '%s%' " +
                "or lower(studies.short_title ) like '%s%' or lower(studies.assigned_identifier ) like '%s%'");

        assertEquals(size, studies.size());


        for (Study study : studies)

        {
            assertTrue((study.getShortTitle() != null && study.getShortTitle().toLowerCase().contains("s"))
                    || (study.getLongTitle() != null && study.getLongTitle().toLowerCase().contains("s"))
                    || (study.getAssignedIdentifier() != null && study.getAssignedIdentifier().toLowerCase().contains("s")));
        }

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

    public void testSearchStudiesBySite() {
        type = "site";
        text = "1";

        String table = studyAjaxFacade.searchStudies(parameterMap, type, text, request);
        assertNotNull(table);

    }

    public void setStudyAjaxFacade(StudyAjaxFacade studyAjaxFacade) {
        this.studyAjaxFacade = studyAjaxFacade;
    }

}
