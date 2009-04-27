package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

//
/**
 * The Class StudyRepository.
 *
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class StudyOrganizationClinicalStaffRepository implements Repository<StudyOrganizationClinicalStaff, StudyOrganizationClinicalStaffQuery> {
    private GenericRepository genericRepository;


    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }


    public StudyOrganizationClinicalStaff findById(Integer id) {
        return genericRepository.findById(StudyOrganizationClinicalStaff.class, id);
    }

    public StudyOrganizationClinicalStaff save(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        throw new CtcAeSystemException("must not use this method..use ClinicalstaffRepo#save method ");
    }

    public void delete(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
    }

    public Collection<StudyOrganizationClinicalStaff> find(StudyOrganizationClinicalStaffQuery query) {
        return genericRepository.find(query);
    }

    public StudyOrganizationClinicalStaff findSingle(StudyOrganizationClinicalStaffQuery query) {
        return genericRepository.findSingle(query);
    }
}