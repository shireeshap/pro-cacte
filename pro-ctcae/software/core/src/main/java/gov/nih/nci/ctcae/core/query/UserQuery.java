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
    private static String TOKEN = "token";

    public UserQuery() {

        super(queryString);
    }

    /**
     * Filter by clinical staff first name.
     *
     * @param username the first name
     */
    public void filterByUserName(final String username) {
        andWhere("LOWER(user.username) = :" + USER_NAME);
        setParameter(USER_NAME, username.toLowerCase());
    }


    public void filterByUserRole(final Role role) {
        leftJoin("user.userRoles as userRole");
        andWhere("userRole.role = :" + ROLE);
        setParameter(ROLE, role);
    }

    public void filterByUserToken(final String token) {
        andWhere("user.token = :" + TOKEN);
        setParameter(TOKEN, token);
    }

    public void excludeByUserId(Integer userId) {
        if (userId != null) {
            andWhere("user.id <> :id");
            setParameter("id", userId);
        }
    }
}