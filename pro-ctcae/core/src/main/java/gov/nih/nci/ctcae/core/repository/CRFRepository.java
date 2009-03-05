package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.Collection;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public CRF findById(Integer crfId) {
        CRF crf = genericRepository.findById(CRF.class, crfId);
        initializeCollections(crf);
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
            crf.setStatus(CrfStatus.RELEASED);

            Study study = studyRepository.findById(crf.getStudy().getId());
            for (StudySite studySite : study.getStudySites()) {
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
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
            for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
                generateSchedulesFromCrfCalendar(crf, studyParticipantCrf, null);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void generateSchedulesFromCrfCalendar(CRF crf, StudyParticipantCrf studyParticipantCrf, String startDate) throws ParseException {
        Date calendarStartDate;
        ProCtcAECalendar proCtcAECalendar = new ProCtcAECalendar();
        if (StringUtils.isBlank(startDate)) {
            calendarStartDate = crf.getEffectiveStartDate();
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            calendarStartDate = sdf.parse(startDate);
        }
        if (crf != null) {

            if (crf.getCrfCalendars() != null) {
                for (CRFCalendar crfCalendar : crf.getCrfCalendars()) {
                    if (!StringUtils.isBlank(crfCalendar.getRepeatEveryAmount())) {
                        proCtcAECalendar.setGeneralScheduleParameters(Integer.parseInt(crfCalendar.getRepeatEveryAmount()), crfCalendar.getRepeatEveryUnit(), Integer.parseInt(crfCalendar.getDueDateAmount()), crfCalendar.getDueDateUnit(), crfCalendar.getRepeatUntilUnit(), crfCalendar.getRepeatUntilAmount(), calendarStartDate);
                        createSchedule(studyParticipantCrf, proCtcAECalendar, ParticipantSchedule.ScheduleType.GENERAL);
                    }
                }
            }
            if (crf.getCrfCycleDefinitions() != null) {
                for (CRFCycleDefinition crfCycleDefinition : crf.getCrfCycleDefinitions()) {
                    if (crfCycleDefinition.getCrfCycles() != null) {
                        for (CRFCycle crfCycle : crfCycleDefinition.getCrfCycles()) {
                            proCtcAECalendar.setCycleParameters(crfCycleDefinition.getCycleLength(), crfCycle.getCycleDays(), 1, crfCycleDefinition.getCycleLengthUnit(), calendarStartDate);
                            createSchedule(studyParticipantCrf, proCtcAECalendar, ParticipantSchedule.ScheduleType.CYCLE);
                            Calendar c = ProCtcAECalendar.getCalendarForDate(calendarStartDate);
                            c.add(Calendar.DATE, crfCycleDefinition.getCycleLength());
                            calendarStartDate = c.getTime();
                        }
                    }
                }
            }

        }
    }

    private void createSchedule(StudyParticipantCrf studyParticipantCrf, ProCtcAECalendar proCtcAECalendar, ParticipantSchedule.ScheduleType scheduleType) throws ParseException {
        ParticipantSchedule participantSchedule = new ParticipantSchedule();
        participantSchedule.setStudyParticipantCrf(studyParticipantCrf);
        participantSchedule.setFinderRepository(finderRepository);
        participantSchedule.setCalendar(proCtcAECalendar);
        participantSchedule.createSchedules(scheduleType);
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#save(gov.nih.nci.ctcae.core.domain.Persistable)
    */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF save(CRF crf) {
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

        crf = super.save(crf);
        initializeCollections(crf);
        return crf;

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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void initializeCollections(CRF crf) {
        if (crf != null) {
            for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
                studyParticipantCrf.getCrf();
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

            List<CrfPageItem> allCrfPageItems = crf.getAllCrfPageItems();
            for (CrfPageItem crfPageItem : allCrfPageItems) {
                crfPageItem.getProCtcQuestion().getProCtcTerm().getProCtcQuestions();
            }

            Study study = studyRepository.findById(crf.getStudy().getId());
            for (StudySite studySite : study.getStudySites()) {
                studySite.getStudyParticipantAssignments();
            }

            for(CRFCalendar crfCalendar :crf.getCrfCalendars()){
                crfCalendar.getDueDateAmount();                
            }
            for (CRFCycleDefinition crfCycleDefinition : crf.getCrfCycleDefinitions()) {
                crfCycleDefinition.getCrfCycles();
            }
        }
    }

}
