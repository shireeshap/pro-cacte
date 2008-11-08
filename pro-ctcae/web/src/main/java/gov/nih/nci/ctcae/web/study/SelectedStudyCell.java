package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import java.util.HashSet;

/**
 * @author Harsh Agarwal
 * @created Oct 23, 2008
 */
public class SelectedStudyCell implements Cell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    public String getHtmlDisplay(TableModel model, Column column) {
        ColumnBuilder inputBuilder = new ColumnBuilder(column);
        inputBuilder.tdStart();

        try {
            Study bean = (Study) model.getCurrentRowBean();
            HashSet<Integer> studyIdSet = (HashSet<Integer>) model.getContext()
                    .getParameterMap().get("participant.studies");

            if (studyIdSet == null) {
                studyIdSet = new HashSet<Integer>();
            }

            Integer id = bean.getId();
            HtmlBuilder htmlBuilder = inputBuilder.getHtmlBuilder().input(
                    "checkbox").name("studyId").value(id.toString());
            if (studyIdSet.contains(id)) {
                htmlBuilder.checked().disabled();
            }
            inputBuilder.getHtmlBuilder().xclose();
            inputBuilder.tdBody(bean.getAssignedIdentifier());

        } catch (Exception e) {
            e.printStackTrace();
        }
        inputBuilder.tdEnd();

        return inputBuilder.toString().trim();
    }

}