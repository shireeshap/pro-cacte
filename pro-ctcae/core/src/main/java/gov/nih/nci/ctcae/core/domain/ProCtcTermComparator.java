package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

//
/**
 * The Class ParticipantCrfDisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class ProCtcTermComparator implements Comparator<ProCtcTerm> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(ProCtcTerm object, ProCtcTerm object1) {
        return object.getTerm().toLowerCase().compareTo(object1.getTerm().toLowerCase());

    }
}