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
    private static String findRolePrivileges = "SELECT rp from RolePrivilege rp ";

    private static String ROLES = "roleTypes";
    public static final String PRIVILEGE_PARTICIPANT_INBOX = "PRIVILEGE_PARTICIPANT_INBOX";
    private static String PRIVILEGE_NAME = "privilegeName";

    /**
     * Instantiates a new clinical staff query.
     */
    public RolePrivilegeQuery() {
        super(queryString);
    }
    
    public RolePrivilegeQuery(boolean findRolePrivilges){
    	super(findRolePrivileges);
    }
    
    public void filterByRoles(Set<Role> roles) {
        andWhere("rp.role in (:" + ROLES + ")");
        setParameterList(ROLES, roles);
    }
    
    public void leftJoinPrivilege(){
    	leftJoin(" rp.privilege as p");
    }
    
    public void filterForAdmin(){
    	andWhere("rp.privilege.privilegeName not in (:" + PRIVILEGE_PARTICIPANT_INBOX + ")");
    	setParameter(PRIVILEGE_PARTICIPANT_INBOX, PRIVILEGE_PARTICIPANT_INBOX);
    }
    
    public void filterByPrivilegeName(String privilegeName){
    	andWhere("rp.privilege.privilegeName in (:" + PRIVILEGE_NAME + ")");
    	setParameter(PRIVILEGE_NAME, privilegeName);
    }

}