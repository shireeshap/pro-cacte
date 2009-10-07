package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;
import java.util.Date;

//
/**
 * The Class ParticipantCrfDisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class CrfActivityDateComparator implements Comparator<CRF> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(CRF object, CRF object1) {
        Date d1 = object.getActivityDate();
        if (d1 == null) {
            d1 = new Date();
        }
        Date d2 = object1.getActivityDate();
        if (d2 == null) {
            d2 = new Date();
        }

        return d1.compareTo(d2);

    }
}