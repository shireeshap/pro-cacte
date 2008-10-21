package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class DisplayOrderComparator implements Comparator<CrfItem> {
    public int compare(CrfItem object, CrfItem object1) {
        return object.getDisplayOrder().compareTo(object1.getDisplayOrder());

    }
}
