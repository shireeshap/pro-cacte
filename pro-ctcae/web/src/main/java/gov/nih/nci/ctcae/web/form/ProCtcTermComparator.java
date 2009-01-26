package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;

import java.util.Comparator;

/**
 * @author Vinay Kumar
 * @crated Jan 26, 2008
 */

/**
 * remove this class later
 */
public class ProCtcTermComparator implements Comparator<ProCtcTerm> {
    public int compare(ProCtcTerm proCtcTerm, ProCtcTerm proCtcTerm1) {
        return proCtcTerm.getTerm().toLowerCase().compareTo(proCtcTerm1.getTerm().toLowerCase());


    }
}