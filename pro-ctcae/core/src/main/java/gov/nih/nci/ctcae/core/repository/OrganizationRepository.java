package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.OrganizationHavingStudySiteQuery;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

//
/**
 * The Class OrganizationRepository.
 *
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class OrganizationRepository implements Repository<Organization, OrganizationQuery> {

    private GenericRepository genericRepository;

    /**
     * Find organizations for study sites.
     *
     * @return the array list< organization>
     */
    public List<Organization> findOrganizationsForStudySites() {
        OrganizationHavingStudySiteQuery query = new OrganizationHavingStudySiteQuery();
        return genericRepository.find(query);
    }

    public Organization findById(Integer id) {
        Organization organization = genericRepository.findById(Organization.class, id);
        return organization;


    }

    public Organization save(Organization organization) {
        return genericRepository.save(organization);


    }

    public void delete(Organization organization) {
        throw new CtcAeSystemException("delete method not supported");

    }

    public Collection<Organization> find(OrganizationQuery query) {
        return genericRepository.find(query);


    }

    public Organization findSingle(OrganizationQuery query) {
        return genericRepository.findSingle(query);


    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
