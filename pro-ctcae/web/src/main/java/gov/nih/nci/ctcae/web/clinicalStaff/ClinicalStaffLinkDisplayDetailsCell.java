package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;

// TODO: Auto-generated Javadoc
/**
 * The Class ClinicalStaffLinkDisplayDetailsCell.
 * 
 * @author Mehul Gulati
 * Date: Oct 28, 2008
 */
public class ClinicalStaffLinkDisplayDetailsCell implements Cell {

    /* (non-Javadoc)
     * @see org.extremecomponents.table.cell.Cell#getExportDisplay(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Column)
     */
    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    /* (non-Javadoc)
     * @see org.extremecomponents.table.cell.Cell#getHtmlDisplay(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Column)
     */
    public String getHtmlDisplay(TableModel model, Column column) {
        ColumnBuilder inputBuilder = new ColumnBuilder(column);
        inputBuilder.tdStart();

        try {
            ClinicalStaff bean = (ClinicalStaff) model.getCurrentRowBean();
            Integer id = bean.getId();
            inputBuilder.getHtmlBuilder().a("createClinicalStaff?clinicalStaffId=" + id);

            inputBuilder.getHtmlBuilder().xclose();
            inputBuilder.tdBody(bean.getFirstName());
        } catch (Exception e) {

        }
        inputBuilder.tdEnd();

        return inputBuilder.toString().trim();
    }
}
