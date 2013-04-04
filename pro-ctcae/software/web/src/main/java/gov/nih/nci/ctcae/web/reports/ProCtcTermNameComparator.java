package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;

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
public class ProCtcTermNameComparator implements Comparator<ProCtcTerm>, Serializable {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(ProCtcTerm obj, ProCtcTerm obj1 ) {
        return obj.getProCtcTermVocab().getTermEnglish().toLowerCase().compareTo(obj1.getProCtcTermVocab().getTermEnglish().toLowerCase());
    }
}