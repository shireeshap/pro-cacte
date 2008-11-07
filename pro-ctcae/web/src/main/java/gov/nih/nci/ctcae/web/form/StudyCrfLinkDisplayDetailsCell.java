package gov.nih.nci.ctcae.web.form;

import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.domain.CrfStatus;

/**
 * @author Mehul Gulati 
 * Date: Nov 5, 2008
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
           String link2 = model.getContext().getContextPath() + "/pages/form/releaseForm?studyCrfId=";

            if ( bean.getCrf().getStatus().equals(CrfStatus.RELEASED) ){
                cellValue = "<a href=\"" + link1 + id.toString() + "\">" + "Schedule Form" + "</a>";
        }
        else {
                cellValue = "<a href=\"" + link2 + id.toString() + "\">" + "Release Form" + "</a>";

            }
        return cellValue;
    }
}
