package gov.nih.nci.ctcae.core.security.afterinvocation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A filter used to filter Collections.
 *
 * @author Vinay Kumar
 * @since Mar 10, 2009
 */
class CollectionFilterer implements Filterer {
    //~ Static fields/initializers =====================================================================================

    protected static final Log logger = LogFactory.getLog(CollectionFilterer.class);

    //~ Instance fields ================================================================================================

    private Collection collection;

    // collectionIter offers significant performance optimisations (as
    // per security-developer mailing list conversation 19/5/05)
    private Iterator collectionIter;
    private Set removeList;

    //~ Constructors ===================================================================================================

    CollectionFilterer(Collection collection) {
        this.collection = collection;

        // We create a Set of objects to be removed from the Collection,
        // as ConcurrentModificationException prevents removal during
        // iteration, and making a new Collection to be returned is
        // problematic as the original Collection implementation passed
        // to the method may not necessarily be re-constructable (as
        // the Collection(collection) constructor is not guaranteed and
        // manually adding may lose sort order or other capabilities)
        removeList = new HashSet();
    }

    //~ Methods ========================================================================================================

    /**
     * @see org.springframework.security.afterinvocation.Filterer#getFilteredObject()
     */
    public Object getFilteredObject() {
        // Now the Iterator has ended, remove Objects from Collection
        Iterator removeIter = removeList.iterator();

        int originalSize = collection.size();

        while (removeIter.hasNext()) {
            collection.remove(removeIter.next());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Original collection contained " + originalSize + " elements; now contains "
                    + collection.size() + " elements");
        }

        return collection;
    }

    /**
     * @see org.springframework.security.afterinvocation.Filterer#iterator()
     */
    public Iterator iterator() {
        collectionIter = collection.iterator();

        return collectionIter;
    }

    /**
     * @see org.springframework.security.afterinvocation.Filterer#remove(java.lang.Object)
     */
    public void remove(Object object) {
        removeList.add(object);
    }
}
