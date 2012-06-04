package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.core.domain.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: allareddy
 * Date: 6/1/12
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserCalendarCommand {

    private List<Participant> participants = new ArrayList<Participant>();
    private ProCtcAECalendar proCtcAECalendar = new ProCtcAECalendar();
    private TreeMap<Integer, List<StudyParticipantCrfSchedule>> scheduleDates = new TreeMap();


    public ProCtcAECalendar getProCtcAECalendar() {
        return proCtcAECalendar;
    }

    public void setProCtcAECalendar(ProCtcAECalendar proCtcAECalendar) {
        this.proCtcAECalendar = proCtcAECalendar;
    }

    public TreeMap<Integer, List<StudyParticipantCrfSchedule>> getScheduleDates() {
        return scheduleDates;
    }

    public void setScheduleDates(TreeMap<Integer, List<StudyParticipantCrfSchedule>> scheduleDates) {
        this.scheduleDates = scheduleDates;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void setCurrentMonthScheduleMap() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
          for (Participant participant : participants) {
           List<StudyParticipantCrf> studyParticipantCrfs = participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs();
            if(studyParticipantCrfs!=null && studyParticipantCrfs.size()>0) {
                  ParticipantSchedule participantSchedule = new ParticipantSchedule();
                  participantSchedule.setProCtcAECalendar(proCtcAECalendar);
                  for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
                      participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
                  }
                    for (StudyParticipantCrfSchedule spcs : participantSchedule.getCurrentMonthSchedules()) {
                          List<StudyParticipantCrfSchedule> schedulesOnDate;
                        int day = Integer.parseInt(sdf.format(spcs.getStartDate()));
                        if(scheduleDates.containsKey(day)) {
                          schedulesOnDate = scheduleDates.get(day);
                          }else{
                              schedulesOnDate = new ArrayList<StudyParticipantCrfSchedule>();
                              scheduleDates.put(day, schedulesOnDate);
                          }
                          schedulesOnDate.add(spcs);
                      }
            }
        }
    }
}
