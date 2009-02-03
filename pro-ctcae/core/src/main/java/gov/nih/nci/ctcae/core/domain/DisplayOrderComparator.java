package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

//
/**
 * The Class DisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class DisplayOrderComparator implements Comparator {

    /**
     * Compare crf page item.
     *
     * @param object  the object
     * @param object1 the object1
     * @return the int
     */
    private int compareCrfPageItem(CrfPageItem object, CrfPageItem object1) {
        return object.getDisplayOrder().compareTo(object1.getDisplayOrder());

    }

    /* (non-Javadoc)
      * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
      */
    public int compare(final Object object, final Object object1) {
        if (object instanceof CrfPageItem) {
            return compareCrfPageItem((CrfPageItem) object, (CrfPageItem) object1);
        } else if (object instanceof CRFPage) {
            return compareCrfPage((CRFPage) object, (CRFPage) object1);
        }

        return 0;

    }

    /**
     * Compare crf page.
     *
     * @param crfPage  the crf page
     * @param crfPage1 the crf page1
     * @return the int
     */
    private int compareCrfPage(final CRFPage crfPage, final CRFPage crfPage1) {
        return crfPage.getPageNumber().compareTo(crfPage1.getPageNumber());


	}
}
