package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class DisplayOrderComparator implements Comparator<CrfPageItem> {
    public int compare(CrfPageItem object, CrfPageItem object1) {
        return object.getDisplayOrder().compareTo(object1.getDisplayOrder());

    }
}
