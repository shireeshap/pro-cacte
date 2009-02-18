package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.RoleType;

import java.util.List;

//
/**
 * User: Vinay Kumar
 * Date: Oct 15, 2008.
 */
public class RoleQuery extends AbstractQuery {

    private static String queryString = "SELECT rs from Roles rs order by rs.id";

    private static String ROLE_TYPES = "roleTypes";


    /**
     * Instantiates a new clinical staff query.
     */
    public RoleQuery() {

        super(queryString);
    }


    public void filterByRoleType(List<RoleType> roleTypes) {

        andWhere("rs.roleType in (:" + ROLE_TYPES + ")");
        setParameterList(ROLE_TYPES, roleTypes);

    }

}