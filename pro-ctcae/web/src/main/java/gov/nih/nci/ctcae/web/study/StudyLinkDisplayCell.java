package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;

//
/**
 * @author Vinay Kumar
 */
public class StudyLinkDisplayCell extends AbstractCell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }


    /* (non-Javadoc)
     * @see org.extremecomponents.table.cell.AbstractCell#getCellValue(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Column)
     */
    protected String getCellValue(TableModel model, Column column) {
        Study bean = (Study) model.getCurrentRowBean();
        Integer id = bean.getId();

        String cellValue = "";
        String editLink = String.format("createStudy?studyId=%s", id.toString());
        String manageStudySitePersonalLink = String.format("manageStudySitePersonal?studyId=%s", id.toString());

        cellValue = String.format(editLink + bean.getAssignedIdentifier() + "</a>");
        cellValue = "<a href=\"" + editLink + "\">" + "Edit" + "</a>";
      
        return cellValue;
    }


}