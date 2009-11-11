package gov.nih.nci.ctcae.web.participant;


import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;


//
/**
 * The Class ParticipantLinkDisplayDetailsCell.
 *
 * @author Harsh Agarwal
 * @created Oct 23, 2008
 */
public class ParticipantLinkDisplayDetailsCell extends AbstractCell {
    protected Log logger = LogFactory.getLog(getClass());

    /* (non-Javadoc)
    * @see org.extremecomponents.table.cell.Cell#getExportDisplay(org.extremecomponents.table.core.TableModel, org.extremecomponents.table.bean.Column)
    */
    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    protected String getCellValue(TableModel tableModel, Column column) {
        Participant bean = (Participant) tableModel.getCurrentRowBean();
        Integer id = bean.getId();
        String cellValue = "<a class=\"fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all\" id=\"participantActions" + id.toString() + "\"><span class=\"ui-icon ui-icon-triangle-1-s\"></span>Actions</a><script>showPopUpMenuParticipant('" + id.toString() + "');</script>";
        return cellValue;
    }

}