package gov.nih.nci.ctcae.core.query;

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
        setParameter(USER_NAME, username);
    }


}