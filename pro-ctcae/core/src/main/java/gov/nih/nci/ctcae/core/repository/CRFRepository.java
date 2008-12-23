package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.util.Date;
import java.text.SimpleDateFormat;

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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateStatusToReleased(CRF crf) {

        crf.setStatus(CrfStatus.RELEASED);

        StudyCrf studyCrf = crf.getStudyCrf();
        if (studyCrf != null) {
            Study study = studyRepository.findById(studyCrf.getStudy().getId());

            for (StudySite studySite : study.getStudySites()) {
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
                    studyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
                    studyCrf.addStudyParticipantCrf(studyParticipantCrf);
                }
            }
        }
        save(crf);
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

}
