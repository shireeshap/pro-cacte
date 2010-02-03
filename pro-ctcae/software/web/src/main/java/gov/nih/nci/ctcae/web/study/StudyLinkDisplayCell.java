package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.springframework.security.context.SecurityContextHolder;

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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean odcOnStudy = user.isODCOnStudy(bean);

        String cellValue = "";
        cellValue = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"studyActions" + id.toString() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuStudy('" + id.toString() + "','" + odcOnStudy + "');</script>";

        return cellValue;
    }


}