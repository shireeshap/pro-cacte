package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.OrganizationClinicalStaffQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

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

    public List<OrganizationClinicalStaff> findByStudyOrganizationId(String text, Integer studyOrganizationId) {
        OrganizationClinicalStaffQuery query = new OrganizationClinicalStaffQuery();
        query.filterByFirstNameOrLastNameOrNciIdentifier(text);
        StudyOrganization studyOrganization = genericRepository.findById(StudyOrganization.class, studyOrganizationId);
        if (studyOrganization != null) {
            Integer organizationId = studyOrganization.getOrganization().getId();
            query.filterByOrganization(organizationId);

            return genericRepository.find(query);
        }

        throw new CtcAeSystemException(String.format("no study organization found for given id %s", studyOrganizationId));
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}