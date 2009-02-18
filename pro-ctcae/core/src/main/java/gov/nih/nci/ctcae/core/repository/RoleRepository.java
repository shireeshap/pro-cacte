package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Roles;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.RoleQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//
/**
 * The Class CRFRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class RoleRepository extends AbstractRepository<Roles, RoleQuery> {


//    protected Roles PI, ODC, LEAD_CRA;

    //
    /* (non-Javadoc)
    * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#getPersistableClass()
    */

    @Override
    protected Class<Roles> getPersistableClass() {
        return Roles.class;


    }

    @Override
    public Roles save(Roles roles) {
        throw new CtcAeSystemException("Can not save/updat the roles");

    }

    public Roles getLeadCRARole() {
        Roles leadCRA = findById(-4);
        return leadCRA;
    }

    public Roles getPI() {
        Roles PI = findById(-3);
        return PI;
    }

    public Roles getOverallDataCoordinator() {
        Roles ODC = findById(-2);
        return ODC;
    }
}