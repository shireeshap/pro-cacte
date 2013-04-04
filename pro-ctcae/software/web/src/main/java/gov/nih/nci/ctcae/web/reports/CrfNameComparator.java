package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.CRF;
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
public class CrfNameComparator implements Comparator<CRF>, Serializable {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(CRF obj, CRF obj1 ) {
        return obj.getTitle().toLowerCase().compareTo(obj1.getTitle().toLowerCase());
    }
}