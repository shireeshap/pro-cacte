package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

//
/**
 * The Class DisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class SeverityLevelComparator implements Comparator<ProCtcValidValue> {


    /* (non-Javadoc)
      * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
      */
    public int compare(final ProCtcValidValue object, final ProCtcValidValue object1) {
        return object.getDisplayOrder().compareTo(object1.getDisplayOrder());

    }
}