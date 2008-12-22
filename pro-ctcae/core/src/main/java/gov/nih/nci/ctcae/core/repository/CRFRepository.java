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

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        CRF tmpCrf = null;
        crf.setStatus(CrfStatus.RELEASED);
        Hashtable<Integer, List<StudyParticipantCrfSchedule>> newSchedulesHash = new Hashtable<Integer, List<StudyParticipantCrfSchedule>>();

        StudyCrf studyCrf = crf.getStudyCrf();
        if (studyCrf != null) {
            Study study = studyRepository.findById(studyCrf.getStudy().getId());

            for (StudySite studySite : study.getStudySites()) {
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {

                        boolean isParentCrf = false;
                        tmpCrf = studyParticipantCrf.getStudyCrf().getCrf();
                        while (tmpCrf.getNextVersionId() != null) {
                            if (tmpCrf.getNextVersionId().equals(crf.getId())) {
                                tmpCrf.setEffectiveEndDate(new Date(crf.getEffectiveStartDate().getTime() -  1000 * 60 * 60 *24));
                                isParentCrf = true;
                                break;
                            } else {
                                tmpCrf = findById(tmpCrf.getNextVersionId());
                            }
                        }

                        if (isParentCrf) {
                            List<StudyParticipantCrfSchedule> removeSchedules = new ArrayList<StudyParticipantCrfSchedule>();
                            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                                if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)) {
                                    if (crf.getEffectiveStartDate().before(studyParticipantCrfSchedule.getStartDate()) ||
                                            dateFormat.format(studyParticipantCrfSchedule.getStartDate()).equals(dateFormat.format(crf.getEffectiveStartDate()))) {
                                        StudyParticipantCrfSchedule newSchedule = new StudyParticipantCrfSchedule();

                                        newSchedule.setStartDate(studyParticipantCrfSchedule.getStartDate());
                                        newSchedule.setDueDate(studyParticipantCrfSchedule.getDueDate());
                                        newSchedule.setStatus(CrfStatus.SCHEDULED);
                                        if (newSchedulesHash.get(studyParticipantAssignment.getId()) == null) {
                                            newSchedulesHash.put(studyParticipantAssignment.getId(), new ArrayList<StudyParticipantCrfSchedule>());
                                        }
                                        newSchedulesHash.get(studyParticipantAssignment.getId()).add(newSchedule);
                                        removeSchedules.add(studyParticipantCrfSchedule);
                                    }
                                }
                            }
                            for(StudyParticipantCrfSchedule studyParticipantCrfSchedule : removeSchedules){
                                studyParticipantCrf.removeCrfSchedule(studyParticipantCrfSchedule);
                            }
                        }
                    }
                }
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
                    studyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
                    studyCrf.addStudyParticipantCrf(studyParticipantCrf);
                    if (newSchedulesHash.containsKey(studyParticipantAssignment.getId())) {
                        List<StudyParticipantCrfSchedule> newSchedules = newSchedulesHash.get(studyParticipantAssignment.getId());
                        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : newSchedules) {
                            studyParticipantCrf.addStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
                        }
                    }
                }
            }
        }
        if (tmpCrf != null)
            save(tmpCrf);
        save(crf);
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

}
