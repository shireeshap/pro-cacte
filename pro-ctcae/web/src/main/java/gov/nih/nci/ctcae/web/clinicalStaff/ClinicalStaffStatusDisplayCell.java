package gov.nih.nci.ctcae.web.clinicalStaff;

import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;

/**
 * Created by IntelliJ IDEA.
 * @author Mehul Gulati
 * Date: Dec 4, 2009
 */

public class ClinicalStaffStatusDisplayCell extends AbstractCell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    protected String getCellValue(TableModel tableModel, Column column) {

        ClinicalStaff bean = (ClinicalStaff) tableModel.getCurrentRowBean();        

        String cellValue = "Effectively " + bean.getStatus().getDisplayName() + " from " + bean.getFormattedDate();
        return cellValue;
    }
}
