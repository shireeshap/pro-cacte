package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CtcCategory;

import java.util.Comparator;

/**
 * @author Vinay Kumar
 * @crated Nov 6, 2008
 */

/**
 * remove this class later
 */
public class CtcCAtegoryComparator implements Comparator<CtcCategory> {
    public int compare(CtcCategory ctcCategory, CtcCategory ctcCategory1) {
        return ctcCategory.getName().toLowerCase().compareTo(ctcCategory1.getName().toLowerCase());


    }
}
