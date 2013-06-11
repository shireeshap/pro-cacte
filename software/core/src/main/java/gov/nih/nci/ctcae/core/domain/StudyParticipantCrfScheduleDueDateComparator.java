package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

//
/**
 * The Class ParticipantCrfDisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class StudyParticipantCrfScheduleDueDateComparator implements Comparator<StudyParticipantCrfSchedule> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(StudyParticipantCrfSchedule object, StudyParticipantCrfSchedule object1) {
        if (object.getDueDate() != null && object1.getDueDate() != null) {
            return object.getDueDate().compareTo(object1.getDueDate());
        }
        return 0;

    }
}