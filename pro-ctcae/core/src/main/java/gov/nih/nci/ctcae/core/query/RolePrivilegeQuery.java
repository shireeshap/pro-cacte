package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Role;

import java.util.Set;

//
/**
 * User: Vinay Kumar
 * Date: Oct 15, 2008.
 */
public class RolePrivilegeQuery extends AbstractQuery {

    private static String queryString = "SELECT rp.privilege from RolePrivilege rp order by rp.id";

    private static String ROLES = "roleTypes";


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

}