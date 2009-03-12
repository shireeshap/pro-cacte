package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
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
public class ClinicalStaffRepository implements Repository<ClinicalStaff, ClinicalStaffQuery> {

    private GenericRepository genericRepository;
    private UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClinicalStaff save(ClinicalStaff clinicalStaff) {
        User user = clinicalStaff.getUser();
        if (user != null) {
            user.setUsername(clinicalStaff.getEmailAddress());
            user = userRepository.save(user);
            clinicalStaff = genericRepository.save(clinicalStaff);
            return clinicalStaff;

        } else {
            throw new CtcAeSystemException("can not save clinical staff without user");
        }


    }

    public void delete(ClinicalStaff clinicalStaff) {


    }

    public Collection<ClinicalStaff> find(ClinicalStaffQuery query) {
        return genericRepository.find(query);


    }

    public ClinicalStaff findSingle(ClinicalStaffQuery query) {
        return genericRepository.findSingle(query);


    }

    public ClinicalStaff findById(Integer id) {
        ClinicalStaff clinicalstaff = genericRepository.findById(ClinicalStaff.class, id);

        Collection<OrganizationClinicalStaff> organizationClinicalStaffCollection = clinicalstaff.getOrganizationClinicalStaffs();
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffCollection) {
            organizationClinicalStaff.getOrganization().getDisplayName();
        }
        return clinicalstaff;


    }


    public List<StudyOrganizationClinicalStaff> findByStudyOrganizationIdAndRole(String text, Integer studyOrganizationId, List<Role> roles) {
        StudyOrganizationClinicalStaffQuery query = new StudyOrganizationClinicalStaffQuery();
        query.filterByFirstNameOrLastNameOrNciIdentifier(text);
        if (roles != null) {
            query.filterByRole(roles);
        }
        query.filterByStudyOrganization(studyOrganizationId);
        return genericRepository.find(query);
    }


    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
