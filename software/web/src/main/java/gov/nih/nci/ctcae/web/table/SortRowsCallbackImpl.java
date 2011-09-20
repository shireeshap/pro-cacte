package gov.nih.nci.ctcae.web.table;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.extremecomponents.table.callback.NullSafeBeanComparator;
import org.extremecomponents.table.callback.SortRowsCallback;
import org.extremecomponents.table.core.TableConstants;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.limit.Sort;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

//
/**
 * The Class SortRowsCallbackImpl.
 *
 * @author Vinay Kumar
 * @since Oct 18, 2008
 */
public class SortRowsCallbackImpl implements SortRowsCallback {

    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(SortRowsCallbackImpl.class);

    /* (non-Javadoc)
     * @see org.extremecomponents.table.callback.SortRowsCallback#sortRows(org.extremecomponents.table.core.TableModel, java.util.Collection)
     */
    public Collection sortRows(TableModel model, Collection rows) throws Exception {
        boolean sorted = model.getLimit().isSorted();

        if (!sorted) {
            return rows;
        }

        Sort sort = model.getLimit().getSort();
        String property = sort.getProperty();
        String sortOrder = sort.getSortOrder();


        if (StringUtils.contains(property, ".")) {
            try {
                if (sortOrder.equals(TableConstants.SORT_ASC)) {
                    Collections.sort((List) rows, new NullSafeBeanComparator(property, new NullStringComparator()));
                } else if (sortOrder.equals(TableConstants.SORT_DESC)) {
                    NullSafeBeanComparator reversedNaturalOrderBeanComparator = new NullSafeBeanComparator(property,
                            new ReverseComparator(new NullStringComparator()));
                    Collections.sort((List) rows, reversedNaturalOrderBeanComparator);
                }
            } catch (NoClassDefFoundError e) {
                String msg = "The column property [" + property + "] is nested and requires BeanUtils 1.7 or greater for proper sorting.";
                logger.error(msg);
                throw new NoClassDefFoundError(msg); //just rethrow so it is not hidden
            }
        } else {
            if (sortOrder.equals(TableConstants.SORT_ASC)) {
                BeanComparator comparator = new BeanComparator(property, new NullStringComparator());
                Collections.sort((List) rows, comparator);
            } else if (sortOrder.equals(TableConstants.SORT_DESC)) {
                BeanComparator reversedNaturalOrderBeanComparator = new BeanComparator(property, new ReverseComparator(new NullStringComparator()));
                Collections.sort((List) rows, reversedNaturalOrderBeanComparator);
            }
        }

        return rows;
    }

}
