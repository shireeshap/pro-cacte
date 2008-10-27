package gov.nih.nci.ctcae.web.participant;



import gov.nih.nci.ctcae.core.domain.Participant;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;


/**
 * @author Harsh Agarwal
 * @created Oct 23, 2008
 */
public class ParticipantLinkDisplayDetailsCell implements Cell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    public String getHtmlDisplay(TableModel model, Column column) {
        ColumnBuilder inputBuilder = new ColumnBuilder(column);
        inputBuilder.tdStart();

        try {
            Participant bean = (Participant) model.getCurrentRowBean();
            Integer id = bean.getId();
            inputBuilder.getHtmlBuilder().a("edit?participantId="+id);
                    
            inputBuilder.getHtmlBuilder().xclose();
            inputBuilder.tdBody(bean.getAssignedIdentifier());

        } catch (Exception e) {
        }
        inputBuilder.tdEnd();

        return inputBuilder.toString().trim();
    }

}