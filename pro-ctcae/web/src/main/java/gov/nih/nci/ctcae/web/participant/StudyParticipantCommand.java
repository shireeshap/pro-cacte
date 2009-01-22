package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class StudyParticipantCommand {

    private Participant participant;
    private Study study;
    StudyParticipantAssignment studyParticipantAssignment;
    private String objectsIndexesToRemove;
    private List<ParticipantSchedule> participantSchedules;
    private FinderRepository finderRepository;

    public StudyParticipantCommand() {
    }

    public Participant getParticipant() {
        return participant;
    }

    public FinderRepository getFinderRepository() {
        return finderRepository;
    }

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Study getStudy() {
        return study;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipantAssignment) {
        this.studyParticipantAssignment = studyParticipantAssignment;
    }

    public String getObjectsIndexesToRemove() {
        return objectsIndexesToRemove;
    }

    public void setObjectsIndexesToRemove(String objectsIndexesToRemove) {
        this.objectsIndexesToRemove = objectsIndexesToRemove;
    }

    public void removeCrfSchedules() {
        Set<String> indexes = org.springframework.util.StringUtils.commaDelimitedListToSet(objectsIndexesToRemove);
        HashMap<StudyParticipantCrf, List<StudyParticipantCrfSchedule>> schedulesToRemove = new HashMap<StudyParticipantCrf, List<StudyParticipantCrfSchedule>>();
        for (String index : indexes) {
            String crfIndex = index.substring(0, index.indexOf('-'));
            String scheduleIndex = index.substring(index.indexOf('-') + 1);

            StudyParticipantCrf crf = studyParticipantAssignment.getStudyParticipantCrfs().get(Integer.parseInt(crfIndex));
            StudyParticipantCrfSchedule crfSchedule = crf.getStudyParticipantCrfSchedules().get(Integer.parseInt(scheduleIndex));

            if (schedulesToRemove.get(crf) == null) {
                schedulesToRemove.put(crf, new ArrayList<StudyParticipantCrfSchedule>());
            }
            schedulesToRemove.get(crf).add(crfSchedule);
        }

        for (StudyParticipantCrf crf : schedulesToRemove.keySet()) {
            for (StudyParticipantCrfSchedule crfSchedule : schedulesToRemove.get(crf)) {
                crf.removeCrfSchedule(crfSchedule);
            }
        }
    }

    public List<ParticipantSchedule> getParticipantSchedules() {
        if (participantSchedules == null) {
            participantSchedules = new ArrayList<ParticipantSchedule>();
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                ParticipantSchedule participantSchedule = new ParticipantSchedule();
                participantSchedule.setStudyParticipantCrf(studyParticipantCrf);
                participantSchedules.add(participantSchedule);
            }
        }

        return participantSchedules;
    }

    public void setParticipantSchedules(List<ParticipantSchedule> participantSchedules) {
        this.participantSchedules = participantSchedules;
    }

    public void createSchedules(int index) throws ParseException {
        ParticipantSchedule participantSchedule = participantSchedules.get(index);

        String repeaEvery = participantSchedule.getRepetitionPeriodUnit();
        int repeatEveryValue = participantSchedule.getRepetitionPeriodAmount();
        String repeatUntil = participantSchedule.getRepeatUntilUnit();
        String repeatUntilValue = participantSchedule.getRepeatUntilValue();
        String dueAfter = participantSchedule.getDueDateUnit();
        int dueAfterValue = participantSchedule.getDueDateAmount();

        Date startDate = participantSchedule.getStartDate();
        int numberOfRepetitions = getNumberOfRepetitions(repeatUntil, repeatUntilValue, startDate, repeaEvery, repeatEveryValue);
        Calendar c = getCalendarForDate(startDate);
        int dueAfterPeriodInMill = getDuePeriodInMillis(dueAfter, dueAfterValue);

        removeSchedules(participantSchedule, startDate);


        for (int i = 0; i < numberOfRepetitions; i++) {
            StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
            studyParticipantCrfSchedule.setStartDate(c.getTime());
            studyParticipantCrfSchedule.setDueDate(new Date(c.getTime().getTime() + dueAfterPeriodInMill));
            CRF crf = finderRepository.findById(CRF.class, participantSchedule.getStudyParticipantCrf().getCrf().getId());
            participantSchedule.getStudyParticipantCrf().addStudyParticipantCrfSchedule(studyParticipantCrfSchedule, crf);
            if ("Days".equals(repeaEvery)) {
                c.add(Calendar.DATE, repeatEveryValue);
            }
            if ("Weeks".equals(repeaEvery)) {
                c.add(Calendar.WEEK_OF_MONTH, repeatEveryValue);
            }
            if ("Months".equals(repeaEvery)) {
                c.add(Calendar.MONTH, repeatEveryValue);
            }
        }

    }

    private Calendar getCalendarForDate(Date date) {
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

        int month = Integer.parseInt(sdfMonth.format(date));
        int day = Integer.parseInt(sdfDay.format(date));
        int year = Integer.parseInt(sdfYear.format(date));

        Calendar c1 = Calendar.getInstance();
        c1.set(year, month - 1, day);
        return c1;
    }


    private int getNumberOfRepetitions(String repeatUntil, String repeatUntilValue, Date startDate, String repeatEvery, int repeatEveryValue) throws ParseException {
        int numberOfRepetitions = 0;
        if ("Number".equals(repeatUntil)) {
            numberOfRepetitions = Integer.parseInt(repeatUntilValue);
        }
        if ("Indefinitely".equals(repeatUntil)) {
            numberOfRepetitions = 100;
        }
        if ("Date".equals(repeatUntil)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date endDate = sdf.parse(repeatUntilValue);
            int numberOfDays = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
            if ("Days".equals(repeatEvery)) {
                numberOfRepetitions = numberOfDays / repeatEveryValue;
            }
            if ("Weeks".equals(repeatEvery)) {
                numberOfRepetitions = numberOfDays / 7;
            }
            if ("Months".equals(repeatEvery)) {
                numberOfRepetitions = numberOfDays / 30;
            }
        }
        return numberOfRepetitions;

    }

    private int getDuePeriodInMillis(String dueAfter, int dueAfterValue) {
        int dueAfterPeriodInMillis = 0;
        if ("Hours".equals(dueAfter)) {
            dueAfterPeriodInMillis = dueAfterValue * 60 * 60 * 1000;
        }
        if ("Days".equals(dueAfter)) {
            dueAfterPeriodInMillis = dueAfterValue * 24 * 60 * 60 * 1000;
        }
        if ("Weeks".equals(dueAfter)) {
            dueAfterPeriodInMillis = dueAfterValue * 7 * 24 * 60 * 60 * 1000;
        }
        return dueAfterPeriodInMillis;
    }

    private void removeSchedules(ParticipantSchedule participantSchedule, Date startDate) {
        List<StudyParticipantCrfSchedule> schedulesToRemove = new ArrayList<StudyParticipantCrfSchedule>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : participantSchedule.getStudyParticipantCrf().getStudyParticipantCrfSchedules()) {
            if (//(sdf.format(studyParticipantCrfSchedule.getStartDate()).equals(sdf.format(startDate)) || studyParticipantCrfSchedule.getStartDate().after(startDate)) &&
                    studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)) {
                schedulesToRemove.add(studyParticipantCrfSchedule);
            }
        }
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedulesToRemove) {
            participantSchedule.getStudyParticipantCrf().removeCrfSchedule(studyParticipantCrfSchedule);
        }
    }
}