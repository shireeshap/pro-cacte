package gov.nih.nci.ctcae.core.domain;

import java.util.Comparator;

//
/**
 * The Class ParticipantCrfDisplayOrderComparator.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class CrfCycleDefinitionOrderComparator implements Comparator<CRFCycleDefinition> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(CRFCycleDefinition object, CRFCycleDefinition object1) {
        return object.getOrder().compareTo(object1.getOrder());

    }
}