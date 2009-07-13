package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.Organization;

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
public class OrganizationNameComparator implements Comparator<Organization> {

    /* (non-Javadoc)
    * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
    */
    public int compare(Organization organization, Organization organization1) {
        return organization.getDisplayName().toLowerCase().compareTo(organization1.getDisplayName().toLowerCase());


    }
}