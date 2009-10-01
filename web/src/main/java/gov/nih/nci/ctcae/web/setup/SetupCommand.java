package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class SetupCommand {

    private User user;

    public SetupCommand() {
        user = new User();
        UserRole userRole = new UserRole();
        userRole.setRole(Role.ADMIN);
        user.addUserRole(userRole);
    }

    public User getUser() {
        return user;


    }

    public void setUser(User user) {
        this.user = user;
    }
}
