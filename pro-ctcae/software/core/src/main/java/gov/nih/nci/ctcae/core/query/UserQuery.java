package gov.nih.nci.ctcae.core.query;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Role;


/**
 * The Class UserQuery.
 */
public class UserQuery extends AbstractQuery {

    private static String USER_NAME = "username";
    private static String ROLE = "role";
    private static String TOKEN = "token";
    private static String MARK_DELETE = "markDelete";

    /**
     * Instantiates a new user query.
     */
    public UserQuery() {
        super(QueryStrings.USER_QUERY_BASIC);
    }

    /**
     * Instantiates a new user query.
     *
     * @param query the query
     */
     public UserQuery(QueryStrings query) {
    	super(query);
    }
    /**
     * Filter by user name.
     *
     * @param username the username
     */
    public void filterByUserName(final String username) {
        andWhere("LOWER(user.username) = :" + USER_NAME);
        setParameter(USER_NAME, username.toLowerCase());
    }


    /**
     * Filter by user role.
     *
     * @param role the role
     */
    public void filterByUserRole(final Role role) {
        leftJoin("user.userRoles as userRole");
        andWhere("userRole.role = :" + ROLE);
        setParameter(ROLE, role);
    }

    /**
     * Filter by user token.
     *
     * @param token the token
     */
    public void filterByUserToken(final String token) {
        andWhere("user.token = :" + TOKEN);
        setParameter(TOKEN, token);
    }

    /**
     * Exclude by user id.
     *
     * @param userId the user id
     */
    public void excludeByUserId(Integer userId) {
        if (userId != null) {
            andWhere("user.id <> :id");
            setParameter("id", userId);
        }
    }

    /**
     * Filter notification by user name.
     *
     * @param userName the user name
     */
    public void filterNotificationByUserName(final String userName) {
        andWhere("LOWER(un.user.username) = :" + USER_NAME);
        andWhere("un.markDelete = :" + MARK_DELETE);
        setParameter(USER_NAME, userName.toLowerCase());
        setParameter(MARK_DELETE, false);
    }
    
    /**
     * Filter in active roles.
     *
     * @param userId the user id
     * @param roleName the role name
     */
    public void filterInActiveRoles(Integer userId, Role roleName){
    	leftJoin(" socs.organizationClinicalStaff ocs left join ocs.clinicalStaff cs left join cs.user u ");
    	andWhere(" u.id = :id");
    	setParameter("id", userId);
    	andWhere("socs.role = :ROLE_NAME");
    	setParameter("ROLE_NAME", roleName);
    	andWhere(" socs.roleStatus = 'ACTIVE'");
    }
    
    
}