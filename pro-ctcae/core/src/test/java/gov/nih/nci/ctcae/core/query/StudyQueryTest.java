package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 */

public class StudyQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        StudyQuery studyQuery = new StudyQuery();
        assertEquals("wrong parsing for constructor",
                "Select study from Study study order by study.shortTitle", studyQuery.getQueryString());

    }

    public void testFilterByAssignedIdentifierExactMatch() throws Exception {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch("John");
        assertEquals("Select study from Study study WHERE lower(study.assignedIdentifier) = :assignedIdentifier order by study.shortTitle",
                studyQuery.getQueryString());
        assertEquals("wrong number of parameters", studyQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", studyQuery.getParameterMap().containsKey("assignedIdentifier"));
        assertEquals("wrong parameter value", studyQuery.getParameterMap().get("assignedIdentifier"), "john");
    }

    public void testFilterByLastName() throws Exception {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesByShortTitle("dow");
        assertEquals("Select study from Study study WHERE lower(study.shortTitle) LIKE :shortTitle order by study.shortTitle",
                studyQuery.getQueryString());
        assertEquals("wrong number of parameters", studyQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", studyQuery.getParameterMap().containsKey("shortTitle"));
        assertEquals("wrong parameter value", studyQuery.getParameterMap().get("shortTitle"), "%dow%");
    }

    public void testFilterBySite() throws Exception {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesForStudySite(1);
        assertEquals("Select study from Study study left join study.studyOrganizations as sso WHERE sso.organization.id = :organizationId AND sso.class = :studySite order by study.shortTitle",
                studyQuery.getQueryString());
        assertEquals("wrong number of parameters", studyQuery.getParameterMap().size(), 2);

    }

}