package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.helper.Fixture;
import junit.framework.TestCase;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * @author Vinay Kumar
 */

public class StudyQueryTest extends TestCase {

    private User user;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        GrantedAuthority[] grantedAuthorities = new GrantedAuthority[]{new GrantedAuthorityImpl(Organization.class.getName() + "." + 1),
                new GrantedAuthorityImpl(Organization.class.getName() + "." + 2), new GrantedAuthorityImpl(Study.class.getName() + "." + 3)};
        user = new User();
        user.setGrantedAuthorities(grantedAuthorities);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, Fixture.DEFAULT_PASSWORD, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(token);

    }

    public void testQueryConstructor() throws Exception {
        StudyQuery studyQuery = new StudyQuery();
        assertEquals("wrong parsing for constructor",
                "Select distinct study from Study study WHERE study.id in (:objectIds ) order by study.shortTitle", studyQuery.getQueryString());

    }

    public void testFilterByAssignedIdentifierExactMatch() throws Exception {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch("John");
        assertEquals("Select distinct study from Study study WHERE lower(study.assignedIdentifier) = :assignedIdentifier AND study.id in (:objectIds ) order by study.shortTitle",
                studyQuery.getQueryString());
        assertEquals("wrong number of parameters", studyQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", studyQuery.getParameterMap().containsKey("assignedIdentifier"));
        assertEquals("wrong parameter value", studyQuery.getParameterMap().get("assignedIdentifier"), "john");
    }

    public void testFilterByLastName() throws Exception {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesByShortTitle("dow");
        assertEquals("Select distinct study from Study study WHERE lower(study.shortTitle) LIKE :shortTitle) AND study.id in (:objectIds ) order by study.shortTitle",
                studyQuery.getQueryString());
        assertEquals("wrong number of parameters", studyQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", studyQuery.getParameterMap().containsKey("shortTitle"));
        assertEquals("wrong parameter value", studyQuery.getParameterMap().get("shortTitle"), "%dow%");
    }

    public void testFilterBySite() throws Exception {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesForStudySite(1);
        assertEquals("Select distinct study from Study study left join study.studyOrganizations as sso WHERE (sso.class = :studySite or sso.class = :leadSite ) AND sso.organization.id = :organizationId AND study.id in (:objectIds ) order by study.shortTitle",
                studyQuery.getQueryString());
        assertEquals("wrong number of parameters", studyQuery.getParameterMap().size(), 3);

    }

}