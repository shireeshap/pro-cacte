package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class AdminInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTestCase {

    private User user;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = userRepository.loadUserByUsername(SYSTEM_ADMIN);


    }


    public void testOrganizationInstanceSecurity() throws Exception {
        login(user);

        OrganizationQuery organizationQuery = new OrganizationQuery();

        List<Organization> organizations = (List<Organization>) organizationRepository.find(organizationQuery);

        int numberOfOrganizations = jdbcTemplate.queryForInt("select count(*) from organizations");
        assertFalse("must find organizations", organizations.isEmpty());
        assertEquals("user should all  organizations because this is admin role .", numberOfOrganizations, organizations.size());


    }


}