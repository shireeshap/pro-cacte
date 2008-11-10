package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CRFRepository extends AbstractRepository<CRF, CRFQuery> {

    private StudyRepository studyRepository;


    @Override
    protected Class<CRF> getPersistableClass() {
        return CRF.class;

    }

    public void updateStatusToReleased(CRF crf) {

        crf = findById(crf.getId());

        crf.setStatus(CrfStatus.RELEASED);
        StudyCrf studyCrf = crf.getStudyCrf();

        Study study = studyRepository.findById(studyCrf.getStudy().getId());

        for (StudySite studySite : study.getStudySites()) {
            for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
                studyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
                studyCrf.addStudyParticipantCrf(studyParticipantCrf);
            }
        }
        save(crf);
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

}
