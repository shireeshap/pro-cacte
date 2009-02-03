package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import org.extremecomponents.table.bean.Row;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.interceptor.RowInterceptor;

//
/**
 * The Class ManageFormTableRowInterceptor.
 *
 * @author Harsh Agarwal
 *         Date: Dec 31, 2008
 */

public class ManageFormTableRowInterceptor implements RowInterceptor {

    /* (non-Javadoc)
     * @see org.extremecomponents.table.interceptor.RowInterceptor#addRowAttributes(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Row)
     */
    public void addRowAttributes(TableModel tableModel, Row row) {
    }

    /* (non-Javadoc)
     * @see org.extremecomponents.table.interceptor.RowInterceptor#modifyRowAttributes(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Row)
     */
    public void modifyRowAttributes(TableModel tableModel, Row row) {
        String crfClass = "crf_" + ((CRF) tableModel.getCurrentRowBean()).getId();
        if (isRowEven(row)) {
            row.setStyleClass("even " + crfClass);
        } else {
            row.setStyleClass("odd " + crfClass);
        }

    }

    /**
     * Checks if is row even.
     *
     * @param row the row
     * @return true, if is row even
     */
    public boolean isRowEven(Row row) {
        if (row.getRowCount() != 0 && (row.getRowCount() % 2) == 0) {
            return true;
        }

        return false;
    }

    /**
     * Find out if the column is sitting on an odd row.
     *
     * @param row the row
     * @return true, if checks if is row odd
     */
    public boolean isRowOdd(Row row) {
        if (row.getRowCount() != 0 && (row.getRowCount() % 2) == 0) {
            return false;
        }

        return true;
    }

}
