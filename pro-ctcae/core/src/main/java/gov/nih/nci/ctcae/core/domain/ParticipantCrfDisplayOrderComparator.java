package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

//
/**
 * The Class ParticipantCrfDisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ParticipantCrfDisplayOrderComparator implements Comparator<StudyParticipantCrfItem> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(StudyParticipantCrfItem object, StudyParticipantCrfItem object1) {
        return object.getCrfPageItem().getDisplayOrder().compareTo(object1.getCrfPageItem().getDisplayOrder());

    }
}