package gov.nih.nci.ctcae.web.participant;


import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;


//
/**
 * The Class ParticipantLinkDisplayDetailsCell.
 *
 * @author Harsh Agarwal
 * @created Oct 23, 2008
 */
public class ParticipantLinkDisplayDetailsCell implements Cell {
    protected Log logger = LogFactory.getLog(getClass());

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
            Participant bean = (Participant) model.getCurrentRowBean();
            Integer id = bean.getId();
            inputBuilder.getHtmlBuilder().a("create?id=" + id);

            inputBuilder.getHtmlBuilder().xclose();
            inputBuilder.tdBody(bean.getAssignedIdentifier());

        } catch (Exception e) {
            logger.error("error while generating link " + e.getMessage(), e);
            throw new CtcAeSystemException(e);

        }
        inputBuilder.tdEnd();

        return inputBuilder.toString().trim();
    }

}