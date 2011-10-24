package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Role;

import java.util.Set;

//
/**
 * User: Vinay Kumar
 * Date: Oct 15, 2008.
 */
public class RolePrivilegeQuery extends AbstractQuery {

    private static String queryString = "SELECT distinct(rp.privilege) from RolePrivilege rp ";

    private static String ROLES = "roleTypes";
    public static final String PRIVILEGE_PARTICIPANT_INBOX = "PRIVILEGE_PARTICIPANT_INBOX";


    /**
     * Instantiates a new clinical staff query.
     */
    public RolePrivilegeQuery() {
        super(queryString);
    }


    public void filterByRoles(Set<Role> roles) {
        andWhere("rp.role in (:" + ROLES + ")");
        setParameterList(ROLES, roles);
    }
    
    public void filterForAdmin(){
    	andWhere("rp.privilege.privilegeName not in (:" + PRIVILEGE_PARTICIPANT_INBOX + ")");
    	setParameter(PRIVILEGE_PARTICIPANT_INBOX, PRIVILEGE_PARTICIPANT_INBOX);
    }

}