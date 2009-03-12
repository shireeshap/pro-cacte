package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 12, 2009
 */
public class UserTest extends TestCase {

    private User user;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        GrantedAuthority[] grantedAuthorities = new GrantedAuthority[]{new GrantedAuthorityImpl(Organization.class.getName() + "." + 1),
                new GrantedAuthorityImpl(Organization.class.getName() + "." + 2)};
        user = new User();
        user.setGrantedAuthorities(grantedAuthorities);

    }

    public void testGetAllowedOrganizationId() {

        List<Integer> organizationIds = user.getAccessableOrganizationIds();
        assertTrue(organizationIds.contains(1));
        assertTrue(organizationIds.contains(2));
    }
}
