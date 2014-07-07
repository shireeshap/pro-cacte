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
 * @author
 * @since Oct 7, 2008
 */
public class OrganizationQueryTest extends TestCase {
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

        assertTrue(SecuredQuery.class.isAssignableFrom(OrganizationQuery.class));

        OrganizationQuery organizationQuery = new OrganizationQuery();
        assertEquals("wrong parsing for constructor",
                "SELECT distinct(o) from Organization o WHERE o.id in (:objectIds ) order by o.id", organizationQuery
                        .getQueryString());

    }

    public void testFilterByTitle() throws Exception {
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationName("a");

        assertEquals(
                "SELECT distinct(o) from Organization o WHERE lower(o.name) LIKE :name AND o.id in (:objectIds ) order by o.id",
                organizationQuery.getQueryString());

        assertEquals("wrong number of parameters", organizationQuery.getParameterMap().size(), 1);
        assertTrue("missing paramenter name", organizationQuery.getQueryParameterListMap().containsKey(
                "objectIds"));
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "name"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get("name"),
                "%a%");

        organizationQuery.filterByNciInstituteCode("b");
        assertEquals("wrong number of parameters", organizationQuery.getParameterMap().size(), 2);
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "nciInstituteCode"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get(
                "nciInstituteCode"), "%b%");

    }


    public void testFilterByNCICode() throws Exception {
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByNciInstituteCode("a");
        assertEquals(
                "SELECT distinct(o) from Organization o WHERE lower(o.nciInstituteCode) LIKE :nciInstituteCode AND o.id in (:objectIds ) order by o.id",
                organizationQuery.getQueryString());
        assertEquals("wrong number of parameters", organizationQuery.getParameterMap().size(), 1);
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "nciInstituteCode"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get(
                "nciInstituteCode"), "%a%");


    }

    public void testFilterByOrganizationNameOrNciInstituteCode() throws Exception {
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationNameOrNciInstituteCode("a");
        assertEquals(
                "SELECT distinct(o) from Organization o WHERE (lower(o.name) LIKE :name or lower(o.nciInstituteCode) LIKE :nciInstituteCode) AND o.id in (:objectIds ) order by o.id",
                organizationQuery.getQueryString());
        assertEquals("wrong number of parameters", organizationQuery.getParameterMap().size(), 2);
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "nciInstituteCode"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get(
                "nciInstituteCode"), "%a%");
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "name"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get("name"),
                "%a%");


    }
}