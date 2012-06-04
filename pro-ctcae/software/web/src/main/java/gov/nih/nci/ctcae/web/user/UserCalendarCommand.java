package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;

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

    private ProCtcAECalendar proCtcAECalendar;
    private TreeMap<Integer, List<StudyParticipantCrfSchedule>> scheduleDates;


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
}
