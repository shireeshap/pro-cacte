package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//
/**
 * The Class StudyRepository.
 *
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class StudyRepository extends AbstractRepository<Study, StudyQuery> {

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#getPersistableClass()
     */
    @Override
    protected Class<Study> getPersistableClass() {
        return Study.class;

    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Study save(Study study) {
        Study savedStudy = super.save(study);
        initialzeStudy(savedStudy);
        return savedStudy;


    }

    @Override
    public Study findById(Integer id) {
        Study study = super.findById(id);
        initialzeStudy(study);
        return study;


    }

    private void initialzeStudy(Study savedStudy) {
        List<StudyOrganization> studyOrganizations = savedStudy.getStudyOrganizations();
        for (StudyOrganization studyOrganization : studyOrganizations) {
            List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaffList = studyOrganization.getStudyOrganizationClinicalStaffs();
            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : studyOrganizationClinicalStaffList) {
                studyOrganizationClinicalStaff.getStudyOrganization();
            }
        }

        for (StudyClinicalStaff studyClinicalStaff : savedStudy.getStudyClinicalStaffs()) {
            studyClinicalStaff.getOrganizationClinicalStaff();
        }
    }
}