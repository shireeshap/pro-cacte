package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class TextBoxCell.
 * 
 * @author Harsh Agarwal
 * @created Oct 23, 2008
 */
public class TextBoxCell implements Cell {

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
            Study bean = (Study) model.getCurrentRowBean();
            HashMap<Integer, String> participantStudyIdentifierMap = (HashMap<Integer, String>) model.getContext()
                    .getParameterMap().get("participant.participantstudyidentifier");

            if (participantStudyIdentifierMap == null) {
                participantStudyIdentifierMap = new HashMap<Integer, String>();
            }

            Integer id = bean.getId();
            HtmlBuilder htmlBuilder = inputBuilder.getHtmlBuilder().input("text").name(
                    "participantStudyIdentifier" + id.intValue());
            if (participantStudyIdentifierMap.containsKey(id)) {
                htmlBuilder.value(participantStudyIdentifierMap.get(id)).readonly();
            } else {
                htmlBuilder.value("");
            }
            inputBuilder.getHtmlBuilder().xclose();

        } catch (Exception e) {
        }
        inputBuilder.tdEnd();

        return inputBuilder.toString().trim();
    }

}