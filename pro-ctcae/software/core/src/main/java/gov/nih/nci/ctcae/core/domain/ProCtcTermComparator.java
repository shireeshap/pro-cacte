package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

/**
 * The Class ParticipantCrfDisplayOrderComparator. 
 * Sorts based on the order in which they are present in the DB. i.e : Id.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class ProCtcTermComparator implements Comparator<ProCtcTerm> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
	//FIXME: Need a better way to do this.
    public int compare(ProCtcTerm object, ProCtcTerm object1) {
        return object.getId().compareTo(object1.getId());

    }
}