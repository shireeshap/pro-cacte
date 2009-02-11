package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

//
/**
 * The Class ParticipantCrfDisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ParticipantAddedQuestionDisplayOrderComparator implements Comparator<StudyParticipantCrfScheduleAddedQuestion> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(StudyParticipantCrfScheduleAddedQuestion object, StudyParticipantCrfScheduleAddedQuestion object1) {
        return object.getProCtcQuestion().getDisplayOrder().compareTo(object1.getProCtcQuestion().getDisplayOrder());

    }
}