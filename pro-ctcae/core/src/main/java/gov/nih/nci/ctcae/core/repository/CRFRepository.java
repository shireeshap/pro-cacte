package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.text.ParseException;

//
/**
 * The Class CRFRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CRFRepository extends AbstractRepository<CRF, CRFQuery> {

    /**
     * The study repository.
     */
    private StudyRepository studyRepository;

    private FinderRepository finderRepository;

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#getPersistableClass()
     */
    @Override
    protected Class<CRF> getPersistableClass() {
        return CRF.class;

    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#findById(java.lang.Integer)
     */
    @Override
    public CRF findById(Integer crfId) {
        CRF crf = genericRepository.findById(CRF.class, crfId);
        if (crf != null) {
            for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
                studyParticipantCrf.getCrf();
            }
        }

        List<CRFPage> crfPageList = crf.getCrfPagesSortedByPageNumber();
        for (CRFPage crfPage : crfPageList) {
            crfPage.getDescription();
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                crfPageItem.getDisplayOrder();
                for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
                    crfPageItemDisplayRule.getProCtcValidValue();
                }
            }
        }
        return crf;


    }

    /**
     * Update status to released.
     *
     * @param crf the crf
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateStatusToReleased(CRF crf) throws ParseException {

        if (crf != null) {
            crf = findById(crf.getId());
            crf.setStatus(CrfStatus.RELEASED);

            Study study = studyRepository.findById(crf.getStudy().getId());
            for (StudySite studySite : study.getStudySites()) {
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    StudyParticipantCrf  studyParticipantCrf = new StudyParticipantCrf();
                    crf.addStudyParticipantCrf(studyParticipantCrf);
                    studyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
                }
            }

            generateSchedulesFromCrfCalendar(crf);

        }
        save(crf);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void generateSchedulesFromCrfCalendar(CRF crf) throws ParseException {
        if (crf != null) {
            crf = findById(crf.getId());

            if (crf.getCrfCalendars() != null) {
                for (CRFCalendar crfCalendar : crf.getCrfCalendars()) {
                    if (!StringUtils.isBlank(crfCalendar.getRepeatEveryAmount())) {
                        ProCtcAECalendar proCtcAECalendar = new ProCtcAECalendar(Integer.parseInt(crfCalendar.getRepeatEveryAmount()), crfCalendar.getRepeatEveryUnit(), Integer.parseInt(crfCalendar.getDueDateAmount()), crfCalendar.getDueDateUnit(), crfCalendar.getRepeatUntilUnit(), crfCalendar.getRepeatUntilAmount(), crf.getEffectiveStartDate());
                        for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
                            createSchedule(studyParticipantCrf, proCtcAECalendar);
                        }
                    }
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void generateSchedulesFromCrfCalendar(CRF crf, StudyParticipantCrf studyParticipantCrf) throws ParseException {
        if (crf != null) {
            crf = findById(crf.getId());

            if (crf.getCrfCalendars() != null) {
                for (CRFCalendar crfCalendar : crf.getCrfCalendars()) {
                    if (!StringUtils.isBlank(crfCalendar.getRepeatEveryAmount())) {
                        ProCtcAECalendar proCtcAECalendar = new ProCtcAECalendar(Integer.parseInt(crfCalendar.getRepeatEveryAmount()), crfCalendar.getRepeatEveryUnit(), Integer.parseInt(crfCalendar.getDueDateAmount()), crfCalendar.getDueDateUnit(), crfCalendar.getRepeatUntilUnit(), crfCalendar.getRepeatUntilAmount(), crf.getEffectiveStartDate());
                        createSchedule(studyParticipantCrf, proCtcAECalendar);
                    }
                }
            }
        }
    }

    private void createSchedule(StudyParticipantCrf studyParticipantCrf, ProCtcAECalendar proCtcAECalendar) throws ParseException {
        ParticipantSchedule participantSchedule = new ParticipantSchedule();
        participantSchedule.setStudyParticipantCrf(studyParticipantCrf);
        participantSchedule.setFinderRepository(finderRepository);
        participantSchedule.setCalendar(proCtcAECalendar);
        participantSchedule.createSchedules();
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#save(gov.nih.nci.ctcae.core.domain.Persistable)
    */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF save
            (CRF
                    crf) {
        CRF tmp = null;
        if (crf.getParentVersionId() != null) {
            tmp = findById(crf.getParentVersionId());
            while (tmp.getParentVersionId() != null) {
                tmp = findById(tmp.getParentVersionId());
            }
        }

        if (tmp != null && (tmp.getId() != crf.getId())) {
            if (!tmp.getTitle().equals(crf.getTitle())) {
                throw (new CtcAeSystemException("You can not update the title if crf is versioned"));
            }
        }

//        if (crf.getOldTitle() != null) {
//            if ((crf.getParentVersionId() != null || crf.getNextVersionId() != null) && !crf.getOldTitle().equals(crf.getTitle())) {
//                throw (new CtcAeSystemException("You can not update the title if crf is versioned"));
//            }
//
//        }
        return super.save(crf);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Sets the study repository.
     *
     * @param studyRepository the new study repository
     */
    @Required
    public void setStudyRepository
            (StudyRepository
                    studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setFinderRepository
            (FinderRepository
                    finderRepository) {
        this.finderRepository = finderRepository;
    }

}
