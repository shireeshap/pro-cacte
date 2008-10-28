package gov.nih.nci.ctcae.web.investigator;

import org.extremecomponents.table.cell.Cell;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.view.html.ColumnBuilder;
import gov.nih.nci.ctcae.core.domain.Investigator;

/**
 * @author Mehul Gulati
 * Date: Oct 28, 2008
 */
public class InvestigatorLinkDisplayDetailsCell implements Cell {

    public String getExportDisplay(TableModel model, Column column) {
        return column.getValueAsString();
    }

    public String getHtmlDisplay(TableModel model, Column column) {
        ColumnBuilder inputBuilder = new ColumnBuilder(column);
        inputBuilder.tdStart();

        try {
            Investigator bean = (Investigator) model.getCurrentRowBean();
            Integer id = bean.getId();
            inputBuilder.getHtmlBuilder().a("createInvestigator?investigatorId="+id);

            inputBuilder.getHtmlBuilder().xclose();
            inputBuilder.tdBody(bean.getFirstName());
        } catch (Exception e) {

        }
        inputBuilder.tdEnd();

        return inputBuilder.toString().trim();
    }
}
