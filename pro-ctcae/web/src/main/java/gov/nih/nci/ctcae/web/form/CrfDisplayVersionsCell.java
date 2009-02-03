package gov.nih.nci.ctcae.web.form;

import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;
import gov.nih.nci.ctcae.core.domain.CRF;

// TODO: Auto-generated Javadoc
/**
 * The Class CrfDisplayVersionsCell.
 * 
 * @author Mehul Gulati
 * Date: Dec 30, 2008
 */
public class CrfDisplayVersionsCell extends AbstractCell implements Cell {

    /* (non-Javadoc)
     * @see org.extremecomponents.table.cell.AbstractCell#getExportDisplay(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Column)
     */
    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    /* (non-Javadoc)
     * @see org.extremecomponents.table.cell.AbstractCell#getCellValue(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Column)
     */
    protected String getCellValue(TableModel model, Column column) {
        CRF bean = (CRF) model.getCurrentRowBean();
        Integer id = bean.getId();
        String cellValue = "";
        if (bean.getParentVersionId() != null) {
            cellValue = "<a href=\"javascript:showVersionForm('" + id.toString() + "')\"><img src=\"../../images/arrow-right.png\" id=\"crfVersionShowImage_" + id + "\"/></a>";
            cellValue = cellValue + "<a href=\"javascript:hideVersionForm('" + id.toString() + "')\"><img src=\"../../images/arrow-down.png\" style=\"display:none;\" id=\"crfVersionHideImage_" + id + "\"/></a>";
        }
        return cellValue;

    }

}