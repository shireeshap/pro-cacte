package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.OrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;
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
public class ClinicalStaffRepository extends AbstractRepository<ClinicalStaff, ClinicalStaffQuery> {

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#getPersistableClass()
     */
    @Override
    protected Class<ClinicalStaff> getPersistableClass() {
        return ClinicalStaff.class;
    }

    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClinicalStaff save(ClinicalStaff clinicalStaff) {
        User user = clinicalStaff.getUser();
        user.setUsername(clinicalStaff.getEmailAddress());
        user = userRepository.save(user);
        clinicalStaff = super.save(clinicalStaff);

        return clinicalStaff;


    }

    @Override
    public ClinicalStaff findById(Integer id) {
        ClinicalStaff clinicalstaff = super.findById(id);

        Collection<OrganizationClinicalStaff> organizationClinicalStaffCollection = clinicalstaff.getOrganizationClinicalStaffs();
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffCollection) {
            organizationClinicalStaff.getOrganization().getDisplayName();
        }
        return clinicalstaff;


    }


    public List<OrganizationClinicalStaff> findByStudyOrganizationId(String text, Integer studyOrganizationId) {
        OrganizationClinicalStaffQuery query = new OrganizationClinicalStaffQuery();
        query.filterByFirstNameOrLastNameOrNciIdentifier(text);
        StudyOrganization studyOrganization = genericRepository.findById(StudyOrganization.class, studyOrganizationId);
        if (studyOrganization != null) {
            Integer organizationId = studyOrganization.getOrganization().getId();
            query.filterByOrganization(organizationId);

            return (List<OrganizationClinicalStaff>) genericRepository.find(query);
        }

        throw new CtcAeSystemException(String.format("no study organization found for given id %s", studyOrganizationId));
    }

    public List<StudyOrganizationClinicalStaff> findByStudyOrganizationIdAndRole(String text, Integer studyOrganizationId, List<Role> roles) {
        StudyOrganizationClinicalStaffQuery query = new StudyOrganizationClinicalStaffQuery();
        query.filterByFirstNameOrLastNameOrNciIdentifier(text);
        if (roles != null) {
            query.filterByRole(roles);
        }
        StudyOrganization studyOrganization = genericRepository.findById(StudyOrganization.class, studyOrganizationId);
        if (studyOrganization != null) {
            Integer organizationId = studyOrganization.getOrganization().getId();
            query.filterByOrganization(organizationId);

            return (List<StudyOrganizationClinicalStaff>) genericRepository.find(query);
        }

        throw new CtcAeSystemException(String.format("no study organization found for given id %s", studyOrganizationId));
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
