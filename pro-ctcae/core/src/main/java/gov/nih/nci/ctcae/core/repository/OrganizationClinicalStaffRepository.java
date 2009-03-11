package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

//
/**
 * The Class ClinicalStaffRepository.
 *
 * @author Mehul Gulati
 *         Date: Oct 15, 2008
 */

@Transactional(propagation = Propagation.REQUIRED, readOnly = true)

public class OrganizationClinicalStaffRepository implements Repository<OrganizationClinicalStaff, ClinicalStaffQuery> {

    private GenericRepository genericRepository;

    public OrganizationClinicalStaff findById(Integer id) {

        return genericRepository.findById(OrganizationClinicalStaff.class, id);


    }

    public OrganizationClinicalStaff save(OrganizationClinicalStaff organizationClinicalStaff) {
        return null;


    }

    public void delete(OrganizationClinicalStaff organizationClinicalStaff) {


    }

    public Collection<OrganizationClinicalStaff> find(ClinicalStaffQuery query) {
        return null;


    }

    public OrganizationClinicalStaff findSingle(ClinicalStaffQuery query) {
        return null;


    }


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}