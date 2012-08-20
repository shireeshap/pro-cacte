package gov.nih.nci.ctcae.core.repository.secured;

import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.OrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//
/**
 * The Class ClinicalStaffRepository.
 *
 * @author Mehul Gulati
 *         Date: Oct 15, 2008
 */

@Transactional(propagation = Propagation.REQUIRED, readOnly = true)

public class OrganizationClinicalStaffRepository implements Repository<OrganizationClinicalStaff, OrganizationClinicalStaffQuery> {

    private GenericRepository genericRepository;

    public OrganizationClinicalStaff findById(Integer id) {

        return genericRepository.findById(OrganizationClinicalStaff.class, id);


    }

    public OrganizationClinicalStaff save(OrganizationClinicalStaff organizationClinicalStaff) {
        throw new CtcAeSystemException("must not use this method..use ClinicalstaffRepo#save method ");


    }

    public void delete(OrganizationClinicalStaff organizationClinicalStaff) {
        genericRepository.delete(organizationClinicalStaff);
    }

    public Collection<OrganizationClinicalStaff> find(OrganizationClinicalStaffQuery query) {
        throw new CtcAeSystemException("must not use this method..use findByStudyOrganizationId ");


    }

    public OrganizationClinicalStaff findSingle(OrganizationClinicalStaffQuery query) {
        return genericRepository.findSingle(query);
    }

    public List<OrganizationClinicalStaff> findByStudyOrganizationId(String text, Integer studyOrganizationId) {
        StudyOrganization studyOrganization = genericRepository.findById(StudyOrganization.class, studyOrganizationId);
        if (studyOrganization != null) {
            Integer organizationId = studyOrganization.getOrganization().getId();

            OrganizationClinicalStaffQuery query = new OrganizationClinicalStaffQuery(organizationId);
            query.filterByFirstNameOrLastNameOrNciIdentifier(text);
            return genericRepository.find(query);
        }

        throw new CtcAeSystemException(String.format("no study organization found for given id %s", studyOrganizationId));
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}