package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.view.html.ColumnBuilder;
import org.extremecomponents.util.HtmlBuilder;

import java.util.HashSet;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectedStudyCell.
 *
 * @author Harsh Agarwal
 * @created Oct 23, 2008
 */
public class SelectedStudyCell implements Cell {

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
            logger.error("error while generating link for study. " + e.getMessage(), e);
            throw new CtcAeSystemException(e);

        }
        inputBuilder.tdEnd();

        return inputBuilder.toString().trim();
    }

}