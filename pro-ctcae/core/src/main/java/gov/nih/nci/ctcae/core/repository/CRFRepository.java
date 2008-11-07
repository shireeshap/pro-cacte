package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CRFRepository extends AbstractRepository<CRF, CRFQuery> {

    @Override
    protected Class<CRF> getPersistableClass() {
        return CRF.class;

    }

    public void updateStatusToReleased(CRF crf) {
        crf.setStatus(CrfStatus.RELEASEED);
        StudyCrf studyCrf = crf.getStudyCrf();
        for(StudySite studySite: studyCrf.getStudy().getStudySites()){
            for(StudyParticipantAssignment studyParticipantAssignment: studySite.getStudyParticipantAssignments()){
                StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf(studyCrf);
                studyCrf.addStudyParticipantCrf(studyParticipantCrf);
            }
        }
        save(crf);
    }
}
