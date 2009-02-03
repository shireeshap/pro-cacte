package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;

// TODO: Auto-generated Javadoc
/**
 * The Class CRFLinkDisplayDetailsCell.
 * 
 * @author Mehul Gulati
 * Date: Nov 5, 2008
 */
public class CRFLinkDisplayDetailsCell extends AbstractCell implements Cell {

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
        String link1 = model.getContext().getContextPath() + "/pages/participant/schedulecrf?crfId=";
        String link2 = model.getContext().getContextPath() + "/pages/form/copyForm?crfId=";
        String editLink = model.getContext().getContextPath() + "/pages/form/editForm?crfId=" + id.toString();

        if (bean.getStatus().equals(CrfStatus.RELEASED)) {
            cellValue = "<a href=\"" + link1 + id.toString() + "\">" + "Schedule" + "</a>";
            cellValue = cellValue + " | <a href=\"" + link2 + id.toString() + "\">" + "Copy" + "</a>";
            if (bean.getNextVersionId() == null) {
                cellValue = cellValue + " | <a href=\"javascript:versionForm('" + id.toString() + "')\">" + "Version" + "</a>";
            }
        } else {
            cellValue = "<a href=\"javascript:releaseForm('" + id.toString() + "')\">" + "Release" + "</a>&nbsp;&nbsp;";
            cellValue = cellValue + " | <a href=\"" + link2 + id.toString() + "\">" + "Copy" + "</a>";
            cellValue = cellValue + " | <a href=\"javascript:deleteForm('" + id.toString() + "')\">" + "Delete" + "</a>";

        }
        if (bean.getStatus().equals(CrfStatus.DRAFT)) {
            cellValue = cellValue + " | <a href=\"" + editLink + "\">" + "Edit" + "</a>";

        }
        return cellValue;
    }
}
