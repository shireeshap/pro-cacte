package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Feb 15, 2011
 * Time: 2:07:47 PM
 * To change this template use File | Settings | File Templates.
 */

public class ParticipantDisplayNameComparator implements Comparator<StudyParticipantCrfSchedule> {

    public int compare(StudyParticipantCrfSchedule object, StudyParticipantCrfSchedule object1) {
        int compare = object.getStudyParticipantCrf().getCrf().getStudy().getDisplayName().compareTo(object1.getStudyParticipantCrf().getCrf().getStudy().getDisplayName());
        if (compare == 0) {
            compare = object.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getDisplayName().
                    compareTo(object1.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().getDisplayName());
            if (compare == 0) {
                compare = object.getStudyParticipantCrf().getCrf().getTitle().
                        compareTo(object1.getStudyParticipantCrf().getCrf().getTitle());
                if (compare == 0) {
                    Date s1 = object.getStartDate();
                    if (s1 == null) {
                        s1 = new Date();
                    }
                    Date s2 = object1.getStartDate();
                    if (s2 == null) {
                        s2 = new Date();
                    }
                    compare = s1.compareTo(s2);
                    if (compare == 0) {
                        Date d1 = object.getDueDate();
                        if (d1 == null) {
                            d1 = new Date();
                        }
                        Date d2 = object1.getDueDate();
                        if (d2 == null) {
                            d2 = new Date();
                        }
                        compare = d1.compareTo(d2);
                    }
                }
            }
        }
        return compare;
    }
}