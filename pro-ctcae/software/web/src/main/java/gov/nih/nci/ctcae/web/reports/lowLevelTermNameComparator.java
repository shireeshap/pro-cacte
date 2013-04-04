package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;

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
public class lowLevelTermNameComparator implements Comparator<LowLevelTerm>, Serializable {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(LowLevelTerm obj, LowLevelTerm obj1 ) {
        return obj.getLowLevelTermVocab().getMeddraTermEnglish().toLowerCase().compareTo(obj1.getLowLevelTermVocab().getMeddraTermEnglish().toLowerCase());
    }
}