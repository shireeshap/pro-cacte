package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

//
/**
 * The Class ParticipantCrfDisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class StudyParticipantCrfScheduleStartDateComparator implements Comparator<StudyParticipantCrfSchedule> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(StudyParticipantCrfSchedule object, StudyParticipantCrfSchedule object1) {
        return object.getStartDate().compareTo(object1.getStartDate());

    }
}