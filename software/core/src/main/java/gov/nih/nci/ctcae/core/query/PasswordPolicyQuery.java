package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Role;

//
/**
 * The Class ProCtcTermQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class PasswordPolicyQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT p from PasswordPolicy p ";
    private static String ROLE = "role";

    /**
     * Instantiates a new pro ctc term query.
     */
    public PasswordPolicyQuery() {
        super(queryString);
    }

    public void filterByRole(final Role role) {
        andWhere("p.role = :" + ROLE);
        setParameter(ROLE, role);
    }
}