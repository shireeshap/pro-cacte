package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Nov 12, 2009
 * Time: 1:25:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudyParticipantAssignmentComparator implements Comparator<StudyParticipantAssignment> {

    public int compare(StudyParticipantAssignment o1, StudyParticipantAssignment o2) {
        Date d1 = o1.getParticipant().getCreationDate();
        Date d2 = o2.getParticipant().getCreationDate();
        if (d1 == null) {
            d1 = Calendar.getInstance().getTime();
        }
        if (d2 == null) {
            d2 = Calendar.getInstance().getTime();
        }

        return d1.compareTo(d2);
    }
}
