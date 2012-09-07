package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CtcCategory;

import java.util.Comparator;

//
/**
 * The Class CtcCAtegoryComparator.
 *
 * @author Vinay Kumar
 * @since Nov 6, 2008
 */

/**
 * remove this class later
 */
public class CtcCAtegoryComparator implements Comparator<CtcCategory> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(CtcCategory ctcCategory, CtcCategory ctcCategory1) {
        return ctcCategory.getName().toLowerCase().compareTo(ctcCategory1.getName().toLowerCase());


    }
}
