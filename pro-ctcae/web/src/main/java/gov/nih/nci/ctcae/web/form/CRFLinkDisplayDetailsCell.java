package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;

//
/**
 * The Class CRFLinkDisplayDetailsCell.
 *
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
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


        if (bean.getStatus().equals(CrfStatus.RELEASED)) {
            cellValue = cellValue + appendScheduleFormValue(model, id);
            cellValue = cellValue + appendCopyFormValue(model, id);


            if (bean.getNextVersionId() == null) {
                cellValue = cellValue + appendVersionFormValue(id);
            }
        } else {
            cellValue = cellValue + appendReleaseFormValue(id);
            cellValue = cellValue + appendCopyFormValue(model, id);
            cellValue = cellValue + appendDeleteFormValue(id);

        }
        if (bean.getStatus().equals(CrfStatus.DRAFT)) {
            cellValue = cellValue + appendEditFormValue(model, id);

        }
        return cellValue;
    }

    private String appendVersionFormValue(Integer id) {

        return "<ctcae:urlAuthorize url=\"/pages/form/versionForm\">\n" +
                " | <a href=\"javascript:versionForm('" + id.toString() + "')\">" + "Version" + "</a>" +
                "    </ctcae:urlAuthorize>";
    }

    private String appendScheduleFormValue(TableModel model, Integer id) {
        String link1 = model.getContext().getContextPath() + "/pages/participant/schedulecrf?crfId=";

        return "<ctcae:urlAuthorize url=\"/pages/participant/schedulecrf\">\n" +
                "<a href=\"" + link1 + id.toString() + "\">" + "Schedule" + "</a>" +
                "    </ctcae:urlAuthorize>";
    }

    private String appendCopyFormValue(TableModel model, Integer id) {
        String link2 = model.getContext().getContextPath() + "/pages/form/copyForm?crfId=";

        return "<ctcae:urlAuthorize url=\"/pages/form/copyForm\">\n" +
                " | <a href=\"" + link2 + id.toString() + "\">" + "Copy" + "</a>" +
                "    </ctcae:urlAuthorize>";
    }

    private String appendReleaseFormValue(Integer id) {

        return "<ctcae:urlAuthorize url=\"/pages/form/releaseForm\">\n" +
                "<a href=\"javascript:releaseForm('" + id.toString() + "')\">" + "Release" + "</a>&nbsp;&nbsp;" +
                "    </ctcae:urlAuthorize>";
    }

    private String appendDeleteFormValue(Integer id) {

        return "<ctcae:urlAuthorize url=\"/pages/form/deleteForm\">\n" +
                " | <a href=\"javascript:deleteForm('" + id.toString() + "')\">" + "Delete" + "</a>" +
                "    </ctcae:urlAuthorize>";


    }

    private String appendEditFormValue(TableModel model, Integer id) {
        String editLink = model.getContext().getContextPath() + "/pages/form/editForm?crfId=" + id.toString();

        return "<ctcae:urlAuthorize url=\"/pages/form/editForm\">\n" +
                " | <a href=\"" + editLink + "\">" + "Edit" + "</a>" +
                "    </ctcae:urlAuthorize>";
    }
}
