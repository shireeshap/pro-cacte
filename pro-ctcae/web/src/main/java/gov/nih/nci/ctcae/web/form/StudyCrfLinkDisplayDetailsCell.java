package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;

/**
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class StudyCrfLinkDisplayDetailsCell extends AbstractCell implements Cell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }


    protected String getCellValue(TableModel model, Column column) {
        StudyCrf bean = (StudyCrf) model.getCurrentRowBean();
        Integer id = bean.getId();

        String cellValue = "";
        String link1 = model.getContext().getContextPath() + "/pages/participant/schedulecrf?studyCrfId=";

        if (bean.getCrf().getStatus().equals(CrfStatus.RELEASED)) {
            cellValue = "<a href=\"" + link1 + id.toString() + "\">" + "Schedule" + "</a>";
            cellValue = cellValue + " | <a href=\"javascript:copyForm('" +id.toString() + "')\">" + "Copy" + "</a>";

        } else {
            cellValue = "<a href=\"javascript:releaseForm('" + id.toString() + "')\">" + "Release" + "</a>";
            cellValue = cellValue + " | <a href=\"javascript:copyForm('" +id.toString() + "')\">" + "Copy" + "</a>";

        }
        return cellValue;
    }
}
