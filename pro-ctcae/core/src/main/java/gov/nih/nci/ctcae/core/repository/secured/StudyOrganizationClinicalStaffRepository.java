package gov.nih.nci.ctcae.core.repository.secured;

import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

//
/**
 * The Class StudyRepository.
 *
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */

@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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
        return genericRepository.save(studyOrganizationClinicalStaff);
    }

    public void delete(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
    }

    public List<StudyOrganizationClinicalStaff> find(StudyOrganizationClinicalStaffQuery query) {
        return genericRepository.find(query);
    }

    public StudyOrganizationClinicalStaff findSingle(StudyOrganizationClinicalStaffQuery query) {
        return genericRepository.findSingle(query);
    }
}