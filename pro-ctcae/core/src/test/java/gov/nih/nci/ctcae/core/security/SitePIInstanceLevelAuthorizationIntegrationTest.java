package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.Role;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class SitePIInstanceLevelAuthorizationIntegrationTest extends SiteCRAInstanceLevelAuthorizationIntegrationTest {
    @Override
    protected Role getRole() {
        return Role.SITE_PI;

    }
}