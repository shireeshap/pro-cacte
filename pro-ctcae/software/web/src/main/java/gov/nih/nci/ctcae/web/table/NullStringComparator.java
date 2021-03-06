package gov.nih.nci.ctcae.web.table;

import org.apache.commons.collections.comparators.NullComparator;

import java.util.Comparator;

//
/**
 * A Comparator that will compare nulls to be either lower or higher than
 * other String objects.
 *
 * @author Vinay Kumar
 * @since Oct 18, 2008
 */
public class NullStringComparator extends NullComparator {

    /**
     * The string comparator.
     */
    private Comparator<String> stringComparator;


    /**
     * Instantiates a new null string comparator.
     */
    public NullStringComparator() {
        super();
        this.stringComparator = String.CASE_INSENSITIVE_ORDER;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.collections.comparators.NullComparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        if (o1 != null && o2 != null && o1 instanceof String && o2 instanceof String) {


            return stringComparator.compare((String) o1, (String) o2);
        } else {
            return super.compare(o1, o2);
        }
    }


    /**
     * Gets the string comparator.
     *
     * @return the string comparator
     */
    public Comparator<String> getStringComparator() {
        return stringComparator;
    }

}
