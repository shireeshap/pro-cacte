package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Role;

//
/**
 * User: Mehul Gulati
 * Date: Oct 15, 2008.
 */
public class UserQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT user from User user order by user.id";

    private static String USER_NAME = "username";
    private static String ROLE = "role";

    public UserQuery() {

        super(queryString);
    }

    /**
     * Filter by clinical staff first name.
     *
     * @param username the first name
     */
    public void filterByUserName(final String username) {
        andWhere("user.username = :" + USER_NAME);
        setParameter(USER_NAME, username.toLowerCase());
    }

    public void filterByUserRole(final Role role) {
        leftJoin("user.userRoles as userRole");
        andWhere("userRole.role = :" + ROLE);
        setParameter(ROLE, role);
    }


}