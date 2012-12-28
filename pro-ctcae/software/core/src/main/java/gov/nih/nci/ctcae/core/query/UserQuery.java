package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.UserRole;

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
    private static String queryString1 = "SELECT un from UserNotification un";
    private static String queryString2 = "SELECT count(distinct un) from UserNotification un";
    private static String queryString3 = "SELECT count(*) from StudyOrganizationClinicalStaff socs";
    private static String USER_NAME = "username";
    private static String ROLE = "role";
    private static String ROLE_NAME = "roleName";
    private static String TOKEN = "token";
    private static String MARK_DELETE = "markDelete";

    public UserQuery() {
        super(queryString);
    }

    public UserQuery(boolean getNotification) {
        super(queryString1);
    }

    public UserQuery(boolean notification, boolean count) {
        super(queryString2);
    }

    public UserQuery(boolean notification, boolean count, boolean checkActiveRole){
    	super(queryString3);
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

    public void filterNotificationByUserName(final String userName) {
        andWhere("LOWER(un.user.username) = :" + USER_NAME);
        andWhere("un.markDelete = :" + MARK_DELETE);
        setParameter(USER_NAME, userName.toLowerCase());
        setParameter(MARK_DELETE, false);
    }
    
    public void filterInActiveRoles(Integer userId, Role roleName){
    	leftJoin(" socs.organizationClinicalStaff ocs left join ocs.clinicalStaff cs left join cs.user u ");
    	andWhere(" u.id = :id");
    	setParameter("id", userId);
    	andWhere("socs.role = :ROLE_NAME");
    	setParameter("ROLE_NAME", roleName);
    	andWhere(" socs.roleStatus = 'ACTIVE'");
    }
    
    
}