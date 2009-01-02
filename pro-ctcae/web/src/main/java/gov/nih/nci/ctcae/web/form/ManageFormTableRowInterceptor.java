package gov.nih.nci.ctcae.web.form;

import org.extremecomponents.table.interceptor.RowInterceptor;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.core.TableModelImpl;
import org.extremecomponents.table.bean.Row;
import gov.nih.nci.ctcae.core.domain.CRF;

/**
 * @author Harsh Agarwal
 *         Date: Dec 31, 2008
 */

public class ManageFormTableRowInterceptor implements RowInterceptor {

    public void addRowAttributes(TableModel tableModel, Row row) {
    }

    public void modifyRowAttributes(TableModel tableModel, Row row) {
        String crfClass="crf_"+((CRF)tableModel.getCurrentRowBean()).getId();
        if (isRowEven(row)) {
            row.setStyleClass("even "+crfClass);
        } else{
            row.setStyleClass("odd "+crfClass);
        }

    }

    public boolean isRowEven(Row row) {
        if (row.getRowCount() != 0 && (row.getRowCount() % 2) == 0) {
            return true;
        }

        return false;
    }

    /**
     * Find out if the column is sitting on an odd row.
     */
    public boolean isRowOdd(Row row) {
        if (row.getRowCount() != 0 && (row.getRowCount() % 2) == 0) {
            return false;
        }

        return true;
    }

}
