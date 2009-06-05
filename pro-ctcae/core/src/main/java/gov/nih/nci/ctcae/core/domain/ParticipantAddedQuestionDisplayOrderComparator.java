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
        Integer fDisplayOrder = 0;
        Integer sDisplayOrder = 0;

        if (object.getProCtcQuestion() == null && object.getMeddraQuestion() == null) {
            return 0;
        }

        if (object.getProCtcQuestion() == null) {
            fDisplayOrder = object.getMeddraQuestion().getDisplayOrder();
        } else {
            fDisplayOrder = object.getProCtcQuestion().getDisplayOrder();
        }

        if (object1.getProCtcQuestion() == null) {
            sDisplayOrder = object1.getMeddraQuestion().getDisplayOrder();
        } else {
            sDisplayOrder = object1.getProCtcQuestion().getDisplayOrder();
        }
        return fDisplayOrder.compareTo(sDisplayOrder);

    }
}