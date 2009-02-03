package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;

import java.util.*;
import java.text.ParseException;


import javax.servlet.http.HttpServletRequest;


// TODO: Auto-generated Javadoc
/**
 * The Class StudyParticipantCommand.
 */
public class StudyParticipantCommand {

    /** The participant. */
    private Participant participant;
    
    /** The study. */
    private Study study;
    
    /** The study participant assignment. */
    StudyParticipantAssignment studyParticipantAssignment;
    
    /** The participant schedules. */
    private List<ParticipantSchedule> participantSchedules;
    
    /** The finder repository. */
    private FinderRepository finderRepository;
    
    /** The repeatdropdown. */
    private String[] repeatdropdown;

    /**
     * Instantiates a new study participant command.
     */
    public StudyParticipantCommand() {
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
     * Gets the finder repository.
     * 
     * @return the finder repository
     */
    public FinderRepository getFinderRepository() {
        return finderRepository;
    }

    /**
     * Sets the finder repository.
     * 
     * @param finderRepository the new finder repository
     */
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
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
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                ParticipantSchedule participantSchedule = new ParticipantSchedule();
                participantSchedule.setStudyParticipantCrf(studyParticipantCrf);
                participantSchedules.add(participantSchedule);
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

    /**
     * Check repetition.
     * 
     * @param request the request
     * 
     * @throws ParseException the parse exception
     */
    public void checkRepetition(HttpServletRequest request) throws ParseException {
        int i = 0;
        for(String repeat:repeatdropdown){
            if(repeat.equals("No")){
                String startDate = request.getParameter("startDate_"+i);
                String dueDate = request.getParameter("dueDate_"+i);
                participantSchedules.get(0).setFinderRepository(finderRepository);
                participantSchedules.get(0).removeSchedules();
                participantSchedules.get(0).createSchedule(startDate, dueDate);
            }
        }
    }
}