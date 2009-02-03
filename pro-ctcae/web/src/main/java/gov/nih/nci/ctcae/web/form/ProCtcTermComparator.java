package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class ProCtcTermComparator.
 * 
 * @author Vinay Kumar
 * @crated Jan 26, 2008
 */

/**
 * remove this class later
 */
public class ProCtcTermComparator implements Comparator<ProCtcTerm> {
    
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(ProCtcTerm proCtcTerm, ProCtcTerm proCtcTerm1) {
        return proCtcTerm.getTerm().toLowerCase().compareTo(proCtcTerm1.getTerm().toLowerCase());


    }
}