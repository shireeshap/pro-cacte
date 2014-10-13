package gov.nih.nci.ctcae.core.security.afterinvocation;

import java.util.Iterator;

/**
 * Filter strategy interface.
 *
 * @author Vinay Kumar
 * @since Mar 10, 2009
 */
interface Filterer {
    //~ Methods ========================================================================================================

    /**
     * Gets the filtered collection or array.
     *
     * @return the filtered collection or array
     */
    Object getFilteredObject();

    /**
     * Returns an iterator over the filtered collection or array.
     *
     * @return an Iterator
     */
    Iterator iterator();

    /**
     * Removes the the given object from the resulting list.
     *
     * @param object the object to be removed
     */
    void remove(Object object);
}
