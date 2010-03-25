package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.ArrayList;
import java.util.List;


//

/**
 * The Class StudyParticipantCommand.
 */
public class StudyParticipantCommand {

    /**
     * The participant.
     */
    private Participant participant;

    /**
     * The study.
     */
    private Study study;

    /**
     * The study participant assignment.
     */
    StudyParticipantAssignment studyParticipantAssignment;

    /**
     * The participant schedules.
     */
    private List<ParticipantSchedule> participantSchedules;


    /**
     * The repeatdropdown.
     */
    private String[] repeatdropdown;

    /**
     * Instantiates a new study participant command.
     */
    public StudyParticipantCommand() {
        super();
    }

    /**
     * Gets the participant.
     *
     * @return the participant
     */
    public Participant getParticipant() {
        return participant;
    }


    /**
     * Sets the participant.
     *
     * @param participant the new participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Gets the study.
     *
     * @return the study
     */
    public Study getStudy() {
        return study;
    }

    /**
     * Sets the study.
     *
     * @param study the new study
     */
    public void setStudy(Study study) {
        this.study = study;
    }

    /**
     * Gets the study participant assignment.
     *
     * @return the study participant assignment
     */
    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    /**
     * Sets the study participant assignment.
     *
     * @param studyParticipantAssignment the new study participant assignment
     */
    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipantAssignment) {
        this.studyParticipantAssignment = studyParticipantAssignment;
    }

    /**
     * Gets the participant schedules.
     *
     * @return the participant schedules
     */
    public List<ParticipantSchedule> getParticipantSchedules() {
        if (participantSchedules == null) {
            participantSchedules = new ArrayList<ParticipantSchedule>();
            ParticipantSchedule participantSchedule = new ParticipantSchedule();
            participantSchedules.add(participantSchedule);
        }
        for (ParticipantSchedule participantSchedule : participantSchedules) {
            participantSchedule.getStudyParticipantCrfs().clear();
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
            }
        }

        return participantSchedules;
    }

    /**
     * Sets the participant schedules.
     *
     * @param participantSchedules the new participant schedules
     */
    public void setParticipantSchedules(List<ParticipantSchedule> participantSchedules) {
        this.participantSchedules = participantSchedules;
    }

    /**
     * Gets the repeatdropdown.
     *
     * @return the repeatdropdown
     */
    public String[] getRepeatdropdown() {
        return repeatdropdown;
    }

    /**
     * Sets the repeatdropdown.
     *
     * @param repeatdropdown the new repeatdropdown
     */
    public void setRepeatdropdown(String[] repeatdropdown) {
        this.repeatdropdown = repeatdropdown;
    }

    public void lazyInitializeAssignment(GenericRepository genericRepository, boolean save) {
        if (save) {
            for (ParticipantSchedule participantSchedule : getParticipantSchedules()) {
                for (StudyParticipantCrf studyParticipantCrf : participantSchedule.getStudyParticipantCrfs()) {
                    genericRepository.save(studyParticipantCrf);
                }
            }
            studyParticipantAssignment = genericRepository.save(studyParticipantAssignment);
        }
        studyParticipantAssignment = genericRepository.findById(StudyParticipantAssignment.class, studyParticipantAssignment.getId());
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
            lazyInitializeStudyParticipantCrf(studyParticipantCrf);
        }

        getParticipantSchedules();

    }

    private void lazyInitializeStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        studyParticipantCrf.getStudyParticipantCrfSchedules();
        studyParticipantCrf.getStudyParticipantCrfAddedQuestions();
        studyParticipantCrf.getCrf().getCrfPages();
        for (CRFPage crfPage : studyParticipantCrf.getCrf().getCrfPages()) {
            crfPage.getCrfPageItems();
        }
    }
}