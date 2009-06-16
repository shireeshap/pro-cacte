package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.Participant;

import java.io.Serializable;
import java.util.Comparator;

//
/**
 * The Class ProCtcTermComparator.
 *
 * @author Vinay Kumar
 * @since Jan 26, 2008
 */

/**
 * remove this class later
 */
public class ParticipantNameComparator implements Comparator<Participant>, Serializable {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(Participant obj, Participant obj1 ) {
        return obj.getDisplayName().toLowerCase().compareTo(obj1.getDisplayName().toLowerCase());


    }
}